package com.wjc.smarthome.dto.iot;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wjc
 * @Date: 2023-03-31 16:08
 **/
@Data
public class DataRedisDto implements Serializable {
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 产品名称
     */
    private String productKey;

    /**
     * 解析数值
     */
    private String analysisValue;

    /**
     * 原始数值
     */
    private Double driverValue;


    private String time;

    /**
     * 采集时间
     */
    private String collectTime;
}
