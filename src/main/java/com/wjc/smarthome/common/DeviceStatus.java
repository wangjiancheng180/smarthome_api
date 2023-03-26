package com.wjc.smarthome.common;

/**
 * @Author: wjc
 * @Date: 2023-03-01 21:18
 **/
public enum DeviceStatus {
    //设备在线。
    ONLINE("ONLINE",1),
    //设备离线
    OFFLINE("OFFLINE",2),
    //设备未激活
    UNACTIVE("UNACTIVE",3),
    //设备已禁用
    DISABLE("DISABLE",4);

    public String s;

    public int i;

    DeviceStatus(String s, int i) {
            this.s = s;
            this.i = i;
    }
}
