package com.wjc.smarthome.service.iot.impl;

import cn.hutool.core.collection.CollUtil;

import cn.hutool.json.JSONUtil;
import com.wjc.smarthome.common.RedisKeyCounts;
import com.wjc.smarthome.dto.iot.DataRedisDto;
import com.wjc.smarthome.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 王建成
 * @date 2022/12/18--14:21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListenerService implements MessageListener {

    @Resource
    private InfluxDB influxDB;

    @Value("${spring.influx.database}")
    private String database;

    private final RedisService redisService;

    /**
     * 业务处理异步线程池，线程池参数可以根据您的业务特点调整，或者您也可以用其他异步方式处理接收到的消息。
     */
    private ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(50000));

    private ExecutorService dbService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors() * 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(50000));

    @Override
    public void onMessage(Message message) {
        try {
            //1.收到消息之后一定要ACK。
            // 推荐做法：创建Session选择Session.AUTO_ACKNOWLEDGE，这里会自动ACK。
            // 其他做法：创建Session选择Session.CLIENT_ACKNOWLEDGE，这里一定要调message.acknowledge()来ACK。
            // message.acknowledge();
            //2.建议异步处理收到的消息，确保onMessage函数里没有耗时逻辑。
            // 如果业务处理耗时过程过长阻塞住线程，可能会影响SDK收到消息后的正常回调。
            executorService.submit(() -> {
                processMessage(message);
            });
        } catch (Exception e) {
            log.error("submit task occurs exception ", e);
        }
    }

    /**
     * 在这里处理您收到消息后的具体业务逻辑。
     */
    private void processMessage(Message message) {
        try {
            byte[] body = message.getBody(byte[].class);
            String content = new String(body);
            String topic = message.getStringProperty("topic");
            String messageId = message.getStringProperty("messageId");
            if (topic.contains("/as/mqtt/status")) {
                log.info("设备状态改变");
            } else if (topic.contains("/thing/event/property/post")) {
                Map contentMap = JSONUtil.toBean(content, Map.class);
                String deviceName = (String) contentMap.get("deviceName");
                String productKey = (String) contentMap.get("productKey");
                Map<String, Map> items = (Map) contentMap.get("items");
                if (CollUtil.isNotEmpty(items)) {
                    //存储数据
                    dbService.execute(() -> {
                        saveDB(deviceName, productKey, items);
                    });
                    List<DataRedisDto> dataRedisDtos = new ArrayList<>(items.size());
                    items.forEach((key, item) -> {
                        DataRedisDto dataRedisDto = new DataRedisDto();
                        dataRedisDto.setDeviceName(deviceName);
                        dataRedisDto.setDataName(key);
                        dataRedisDto.setDriverValue(Double.valueOf(item.get("value").toString()));
//                        dataRedisDto.setTime(item.get("collectTime").toString());
                        dataRedisDto.setCollectTime(item.get("time").toString());
                        dataRedisDtos.add(dataRedisDto);
                        redisService.save(RedisKeyCounts.DeviceKeys.DEVICE_DATA_PREFIX + deviceName, key, dataRedisDto);
                    });
                    redisService.sendMessage(RedisKeyCounts.MessageChannel.DEVICE_INFO, JSONUtil.toJsonStr(dataRedisDtos));
                }
            }

            log.info("接收到的信息：message"
                    + ",\n topic = " + topic
                    + ",\n messageId = " + messageId
                    + ",\n content = " + content);
        } catch (Exception e) {
            log.error("processMessage occurs error ", e);
        }
    }

    /**
     * 添加设备上报数据到时序数据库influxDB
     *
     * @param deviceName,productKey,items
     */
    private void saveDB(String deviceName, String productKey, Map<String, Map> items) {
        List<Point> points = new ArrayList<>(items.size());
        for (Map.Entry<String, Map> entry : items.entrySet()
        ) {
            Map<String, Long> item = entry.getValue();
            Point point = Point.measurement("data")
                    .tag("product_key", productKey)
                    .tag("device_name", deviceName)
                    .tag("data_name", entry.getKey())
                    .addField("driver_value", item.get("value"))
                    .addField("collect_time",item.get("time").toString())
                    .time(item.get("time"), TimeUnit.MILLISECONDS)
                    .build();
            points.add(point);
        }
        BatchPoints datas = BatchPoints.database(database).consistency(InfluxDB.ConsistencyLevel.ALL).points(points).build();
        influxDB.write(datas);
    }


}
