package com.wjc.smarthome.websocket;

import cn.hutool.core.collection.CollUtil;
import com.wjc.smarthome.common.RedisKeyCounts;
import com.wjc.smarthome.dto.iot.DataRedisDto;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.service.iot.InfluxService;
import com.wjc.smarthome.util.WebSocketUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: wjc
 * @Date: 2023-04-01 16:53
 **/
@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final RedisService redisService;

    private final InfluxService influxService;

    public WSMessage findDeviceData(String deviceName) {
        String hash = RedisKeyCounts.DeviceKeys.DEVICE_DATA_PREFIX + deviceName;
        Set<Object> fileds = redisService.getFileds(hash);
        List<DataRedisDto> dataRedisDtos = new ArrayList<>(fileds.size());
        if (CollUtil.isNotEmpty(fileds)){
            fileds.forEach(filed ->{
                DataRedisDto dataRedisDto = redisService.get(hash, filed.toString(), DataRedisDto.class);
                dataRedisDtos.add(dataRedisDto);
            });
        }else {
            return new WSMessage(WebSocketUtil.DataType.DEVICE_DATA,influxService.findDeviceData(deviceName));
        }
        return new WSMessage(WebSocketUtil.DataType.DEVICE_DATA,dataRedisDtos);
    }
}
