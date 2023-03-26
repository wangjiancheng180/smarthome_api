package com.wjc.smarthome.enetity.iot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author 王建成
 * @date 2022/3/31--10:19
 */
@ApiModel(value = "设备信息")
@TableName(value = "device")
@Data
public class Device extends BaseEnetity {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "设备id")
    private Integer id;

    /**
     * 设备所隶属的产品ProductKey。
     */
    @TableField(value = "product_key")
    @ApiModelProperty(value = "设备所隶属的产品ProductKey。")
    private String productKey;

    /**
     * 设备名称。
     */
    @TableField(value = "device_name")
    @ApiModelProperty(value = "设备名称。")
    private String deviceName;

    /**
     * ONLINE：设备在线。
     * OFFLINE：设备离线。
     * UNACTIVE：设备未激活。
     * DISABLE：设备已禁用。
     */
    @TableField(value = "device_status")
    @ApiModelProperty(value = "ONLINE：设备在线 1。,OFFLINE：设备离线 2。,UNACTIVE：设备未激活 3。,DISABLE：设备已禁用 4。")
    private Integer deviceStatus;

    /**
     * 设备密钥。
     */
    @TableField(value = "device_secret")
    @ApiModelProperty(value = "	,设备密钥。")
    private String deviceSecret;

    /**
     * 设备的备注名称。
     */
    @TableField(value = "nick_name")
    @ApiModelProperty(value = "设备的备注名称。")
    private String nickName;

    /**
     * 设备ID。物联网平台为该设备颁发的ID，设备的唯一标识符。
     */
    @TableField(value = "iot_id")
    @ApiModelProperty(value = "设备ID。物联网平台为该设备颁发的ID，设备的唯一标识符。")
    private String iotId;

    /**
     * 启用：1，禁用2。
     */
    @TableField(value = "disable")
    @ApiModelProperty(value = "启用：1，禁用2。")
    private Integer disable;

//    public static final String COL_ID = "id";
//
//    public static final String COL_PRODUCT_KEY = "product_key";
//
//    public static final String COL_DEVICE_NAME = "device_name";
//
//    public static final String COL_DEVICE_STATUS = "device_status";
//
//    public static final String COL_DEVICE_SECRET = "device_secret";
//
//    public static final String COL_NICK_NAME = "nick_name";
//
//    public static final String COL_IOT_ID = "iot_id";

}