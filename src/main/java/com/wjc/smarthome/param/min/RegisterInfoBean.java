package com.wjc.smarthome.param.min;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wjc
 * @Date: 2023-03-26 13:53
 **/
@ApiModel("小程序设备绑定用户信息")
@Data
public class RegisterInfoBean {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("设备名称")
    private String deviceName;
}
