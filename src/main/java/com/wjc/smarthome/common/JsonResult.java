package com.wjc.smarthome.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 王建成
 * @date 2022/3/17--21:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "通用公共返回类")
public class JsonResult <T> implements Serializable {

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAILURED = 500;
    public static final String[] NOOP = new String[] {};

    @ApiModelProperty(value = "返回状态")
    private int code; // 处理状态：0: 成功
    @ApiModelProperty(value = "返回消息")
    private String message;
    private T data; // 返回数据

    /**
     * 有data的正常返回
     *
     * @param data data内容
     * @param <T> data类型
     */
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(CODE_SUCCESS,"success",data);
    }

    public static <T> JsonResult<T> success() {
        return new JsonResult<>(CODE_SUCCESS,"success",null);
    }
    public static <T> JsonResult<T> success(Integer code) {
        return new JsonResult<>(code,"success",null);
    }
    public static <T> JsonResult<T> success(Integer code,String message) {
        return new JsonResult<>(code,message,null);
    }
    public static <T>JsonResult<T> success(Integer code, String message,T data) {
        return new JsonResult<>(code,message,data);
    }
    public static <T> JsonResult<T> success(Integer code,T data) {
        return new JsonResult<>(code,"success",data);
    }

    public static JsonResult failure(String message) {
        return new JsonResult(CODE_FAILURED,message,null);
    }
    public static <T>JsonResult<T> failure(T data) {
        return new JsonResult(CODE_FAILURED,"请求失败！",data);
    }
    public static JsonResult failure(Integer code,String message) {
        return new JsonResult(code,message,null);
    }

    public static <T>  JsonResult<T>  failure(Integer code,String message,T data) {
        return new JsonResult(code,message,data);
    }

    public static  <T> JsonResult<T> failure(String message,T data){
        return new JsonResult(CODE_FAILURED,message,data);
    }
}
