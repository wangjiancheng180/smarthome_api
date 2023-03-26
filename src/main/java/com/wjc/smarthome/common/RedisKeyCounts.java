package com.wjc.smarthome.common;

/**
 * @Author: wjc
 * @Date: 2023-03-12 10:45
 **/
public interface RedisKeyCounts {

    interface DeviceKeys {

        /**
         * 设备在线状态
         */
        Integer ONLINE_STATUS = 1;
        /**
         * 设备离线状态
         */
        Integer OFFLINE_STATUS = 2;
        /**
         * 在线设备列表
         */
        String DEVICES_ONLINE = "devices:online";
        /**
         * 离线设备列表
         */
        String DEVICES_OFFLINE = "devices:offline";
    }
}
