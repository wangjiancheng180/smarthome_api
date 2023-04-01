package com.wjc.smarthome.service.iot;

import com.wjc.smarthome.dto.iot.DataRedisDto;

import java.util.List;

/**
 * @Author: wjc
 * @Date: 2023-04-01 17:55
 **/
public interface InfluxService {
    /**
     * 获取设备数据
     * @param deviceName
     * @return
     */
    List<DataRedisDto> findDeviceData(String deviceName);
}
