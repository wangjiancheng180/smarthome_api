package com.wjc.smarthome.enetity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wjc.smarthome.enetity.BaseEnetity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import lombok.Data;

/**
*@Author: wjc
*@Date: 2023-03-11 12:26
**/    
@ApiModel(value="com-wjc-smarthome-enetity-system-SysLogInfo")
@Data
@TableName(value = "sys_log_info")
public class SysLogInfo extends BaseEnetity {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Integer id;

    /**
     * 请求来源ip
     */
    @TableField(value = "ip")
    @ApiModelProperty(value="请求来源ip")
    private String ip;

    /**
     * 请求参数
     */
    @TableField(value = "method")
    @ApiModelProperty(value="请求参数")
    private String method;

    /**
     * 请求模块
     */
    @TableField(value = "module")
    @ApiModelProperty(value="请求模块")
    private String module;

    /**
     * 0：操作日志，1：登录日志
     */
    @TableField(value = "log_type")
    @ApiModelProperty(value="0：操作日志，1：登录日志")
    private Integer logType;

    /**
     *  操作类型
    0、查询
    1、添加
    2、修改
    3、删除
     
     */
    @TableField(value = "operate_type")
    @ApiModelProperty(value=" 操作类型,    0、查询,    1、添加,    2、修改,    3、删除,     ")
    private Integer operateType;

    /**
     * 请求路径
     */
    @TableField(value = "url")
    @ApiModelProperty(value="请求路径")
    private String url;

    /**
     * 请求参数
     */
    @TableField(value = "params")
    @ApiModelProperty(value="请求参数")
    private String params;

    /**
     * 执行状态
     */
    @TableField(value = "exe_status")
    @ApiModelProperty(value="执行状态，1：正常，2：异常")
    private Integer exeStatus;

    /**
     * 错误信息
     */
    @TableField(value = "error_msg")
    @ApiModelProperty(value="异常信息")
    private String errorMsg;


    /**
     * 日志开始时间
     */
    @TableField(value = "start_time")
    @ApiModelProperty(value="日志开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 日志结束时间
     */
    @TableField(value = "end_time")
    @ApiModelProperty(value="日志结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}