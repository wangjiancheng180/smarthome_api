package com.wjc.smarthome.param.iot;

import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author: wjc
 * @Date: 2023-03-01 20:39
 **/
@ApiModel("新增设备model")
@Data
public class DeviceCreateBean extends BaseEnetity {
    /**
     * 设备名称。
     */
    @ApiModelProperty(value = "设备名称。")
    @NotNull
    @Size(max =32,min =4,message ="设备名称长度限制4-32个字符")
    private String deviceName;

    /**
     * 设备的备注名称。
     */
    @ApiModelProperty(value = "设备的备注名称。")
    @Size(max =64,min =4,message ="设备备注长度限制4-64个字符")
    private String nickName;
}
