package com.wjc.smarthome.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.enetity.system.SysLogInfo;
import com.wjc.smarthome.service.system.SysLogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wjc
 * @Date: 2023-03-12 10:06
 **/
@RestController
@RequestMapping("/sysLoginfo")
@Api(tags = "日志管理")
@RequiredArgsConstructor
public class SysLogInfoController {

    private final SysLogInfoService sysLogInfoService;

    @GetMapping("/queryLogs")
    @ApiOperation("获取日志列表前十条")
    public JsonResult<IPage<SysLogInfo>> queryLogs(){
        return sysLogInfoService.queryLogs();
    }
}
