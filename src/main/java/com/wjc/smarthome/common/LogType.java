package com.wjc.smarthome.common;

import lombok.Getter;

/**
 * @Author: wjc
 * @Date: 2023-03-11 11:19
 **/
@Getter
public enum LogType {
    OPERA(0, "操作日志"),
    LOGIN(1, "登录日志");

    private final int num;

    private final String value;

    LogType(int num, String value) {
        this.num = num;
        this.value = value;
    }

    public static String getValue(int num) {
        for (LogType types : values()) {
            if (types.num == num) {
                return types.getValue();
            }
        }
        return null;
    }
}
