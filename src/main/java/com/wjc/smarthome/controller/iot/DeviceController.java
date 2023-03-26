package com.wjc.smarthome.controller.iot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;
import com.wjc.smarthome.enetity.iot.Device;
import com.wjc.smarthome.param.iot.DeviceCreateBean;
import com.wjc.smarthome.param.iot.DeviceQueryBean;
import com.wjc.smarthome.service.iot.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: wjc
 * @Date: 2023-03-01 20:33
 **/
@RestController
@RequestMapping("/device")
@ApiOperation("设备信息接口")
@Api(tags = "设备管理")
@RequiredArgsConstructor
@Validated
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/create")
    @ApiOperation("创建设备")
    @SystemLog(module = "设备模块",methods = "创建设备",logType = LogType.OPERA,operateType = OperateType.CREATE)
    public JsonResult<Boolean> create(@RequestBody @Valid DeviceCreateBean bean){
        return deviceService.create(bean);
    }

    @PutMapping("/disable")
    @ApiOperation("禁用设备")
    @SystemLog(module = "设备模块",methods = "禁用设备",logType = LogType.OPERA,operateType = OperateType.UPDATE)
    public JsonResult<Boolean> disable(@RequestParam("iotId") @NotNull String iotId){
        return deviceService.disable(iotId);
    }

    @PutMapping("/enable")
    @ApiOperation("启用设备")
    @SystemLog(module = "设备模块",methods = "启用设备",logType = LogType.OPERA,operateType = OperateType.UPDATE)
    public JsonResult<Boolean> enable(@RequestParam("iotId") @NotNull String iotId){
        return deviceService.enable(iotId);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除设备")
    @SystemLog(module = "设备模块",methods = "删除设备",logType = LogType.OPERA,operateType = OperateType.DELETE)
    public JsonResult<Boolean> delete(@RequestParam("iotId") @NotNull String iotId){
        return deviceService.delete(iotId);
    }

    @GetMapping("/queryByIotId")
    @ApiOperation("通过IotId查询")
    @SystemLog(module = "设备模块",methods = "查询设备",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<Device> queryByIotId(@RequestParam("iotId")  @NotNull String iotId){
        return deviceService.queryByIotId(iotId);
    }

    @PostMapping("/queryPage")
    @ApiOperation("分页查询")
    @SystemLog(module = "设备模块",methods = "分页查询设备",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<IPage<Device>> queryPage(@RequestBody @Valid DeviceQueryBean bean){
        return deviceService.queryPage(bean);
    }

    @GetMapping("/queryOnlineDevice")
    @ApiOperation("获取在线设备")
    @SystemLog(module = "设备模块",methods = "获取在线设备",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<List<Device>> queryOnlineDevice(){
        return deviceService.queryOnlineDevice();
    }

    @GetMapping("/queryOfflineDevice")
    @ApiOperation("获取离线设备")
    @SystemLog(module = "设备模块",methods = "获取离线设备",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<List<Device>> queryOfflineDevice(){
        return deviceService.queryOfflineDevice();
    }
}
