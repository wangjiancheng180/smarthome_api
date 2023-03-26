package com.wjc.smarthome.enetity.iot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*@Author: wjc
*@Date: 2023-03-26 14:38
**/    
@ApiModel(value="com-wjc-smarthome-enetity-iot-Person")
@Data
@TableName(value = "person")
public class Person {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Integer id;

    @TableField(value = "username")
    @ApiModelProperty(value="")
    private String username;

    @TableField(value = "password")
    @ApiModelProperty(value="")
    private String password;

    /**
     * 地区
     */
    @TableField(value = "area")
    @ApiModelProperty(value="地区")
    private String area;

    @TableField(value = "phone")
    @ApiModelProperty(value="")
    private String phone;
}