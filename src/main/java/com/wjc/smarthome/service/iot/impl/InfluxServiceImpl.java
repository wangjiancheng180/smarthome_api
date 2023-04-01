package com.wjc.smarthome.service.iot.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.wjc.smarthome.dto.iot.DataRedisDto;
import com.wjc.smarthome.service.iot.InfluxService;
import lombok.RequiredArgsConstructor;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: wjc
 * @Date: 2023-04-01 17:54
 **/
@Service
@RequiredArgsConstructor
public class InfluxServiceImpl implements InfluxService {

    private final InfluxDB influxDB;

    @Value("${spring.influx.database}")
    private String database;

    @Override
    public List<DataRedisDto> findDeviceData(String deviceName) {
        String template = "select * from data where device_name = '{}' group by data_name,device_name order by time desc limit 1";
        String sql = StrUtil.format(template, deviceName);
        return fetchResults(sql, DataRedisDto.class);
    }


    /**
     * 查询，返回对象的list集合
     *
     * @param query
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> List<T> fetchResults(String query, Class<?> clasz) {
        //结果集
        List results = new ArrayList<>();
        QueryResult queryResult = influxDB.query(new Query(query, database));
        if (CollUtil.isEmpty(queryResult.getResults())) {
            return results;
        }
        //用来存放数据的object集合这里就是后面反射对象的Map集合
        List<Map<String, Object>> objects = new ArrayList<>();
        queryResult.getResults().forEach(result -> {
            result.getSeries().forEach(serial -> {
                //一条serial代表一个group也就是一种类型的对象
                Map<String, Object> temp = new HashMap<>(serial.getValues().size());
                //先收集tag的字段名和值
                serial.getTags().forEach((tagKey, tagValue) -> {
                    String fieldName = StrUtil.toCamelCase(tagKey);
                    temp.put(fieldName, tagValue);
                });
                //收集其他的字段名和value
                List<String> columns = serial.getColumns();
                int fieldSize = columns.size();
                serial.getValues().forEach((value) -> {
                    HashMap<String, Object> map = new HashMap<>(temp);

                    for (int i = 0; i < fieldSize; i++) {
                        String fieldName1 = columns.get(i);
                        //解决驼峰映射
                        fieldName1 = StrUtil.toCamelCase(fieldName1);
                        map.put(fieldName1, value.get(i));
                    }
                    objects.add(map);
                });
            });
        });
        //反射注入对象属性
        objects.forEach(t -> {
            try {
                Object obj = clasz.newInstance();
                t.forEach((key, value) -> {
                    try {
                        Field field = clasz.getDeclaredField(key);
                        field.setAccessible(true);
                        field.set(obj, value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
                results.add(obj);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return results;
    }
}
