package com.wjc.smarthome.param.iot;

import com.wjc.smarthome.common.PageRequestInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

/**
 * @Author: wjc
 * @Date: 2023-03-02 20:08
 **/
@Data
@ApiModel("设备查询类")
public class DeviceQueryBean extends PageRequestInfo {

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备状态：")
    @DecimalMax("4")
    @DecimalMin("1")
    private Integer deviceStatus;

    @ApiModelProperty(value = "启用：1，禁用2。")
    @DecimalMax("2")
    @DecimalMin("1")
    private Integer disable;

    @ApiModelProperty("备注名称")
    private String nickName;


}
