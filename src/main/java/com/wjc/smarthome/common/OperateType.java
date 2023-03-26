package com.wjc.smarthome.common;

import lombok.Getter;

/**
 * @Author: wjc
 * @Date: 2023-03-11 11:21
 **/
@Getter
public enum OperateType {
    //登录或登出
    EMPTY(-1, ""),
    //查询
    QUERY(0, "查询"),
    //添加
    CREATE(1, "添加"),
    //修改
    UPDATE(2, "修改"),
    //删除
    DELETE(3, "删除"),
    //导出
    EXPORT(4, "导出");

    private final Integer num;

    private final String value;

    OperateType(Integer num, String value) {
        this.num = num;
        this.value = value;
    }

    public static String getValue(Integer num) {
        for (OperateType types : values()) {
            if (types.num.equals(num)) {
                return types.getValue();
            }
        }
        return null;
    }
}
