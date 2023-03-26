package com.wjc.smarthome.controller.count;

import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;
import com.wjc.smarthome.service.count.StatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author: wjc
 * @Date: 2023-03-11 14:49
 **/
@RestController
@RequestMapping("/stats")
@ApiOperation("统计信息接口")
@Api(tags = "统计信息管理")
@RequiredArgsConstructor
@Validated
public class StatsController {


    private final StatsService statsService;

    @GetMapping("/queryDeviceOfWeek")
    @ApiOperation("查询过去7天设备增长数量")
    @SystemLog(module = "统计信息模块",methods = "查询七天设备增长数量",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<Map<String, Integer>> queryDeviceOfWeek(@RequestParam("days") @NotNull Integer days){
       return statsService.queryDeviceOfWeek(days);
    }

    @GetMapping("/queryUserOfWeek")
    @ApiOperation("查询过去7天用户增长数量")
    @SystemLog(module = "统计信息模块",methods = "查询七天设备增长数量",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<Map<String, Integer>> queryUserOfWeek(@RequestParam("days") @NotNull Integer days){
        return statsService.queryUserOfWeek(days);
    }

    @GetMapping("/queryTotalUser")
    @ApiOperation("查询总用户数")
    @SystemLog(module = "统计信息模块",methods = "查询总用户数",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<Integer> queryTotalUser(){
        return statsService.queryTotalUser();
    }

    @GetMapping("/queryTotalDevice")
    @ApiOperation("查询总设备")
    @SystemLog(module = "统计信息模块",methods = "查询总设备",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<Integer> queryTotalDevice(){
        return statsService.queryTotalDevice();
    }



}
