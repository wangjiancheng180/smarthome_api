package com.wjc.smarthome.controller.system;


import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;
import com.wjc.smarthome.controller.BaseController;
import com.wjc.smarthome.dto.system.SysResourceDto;
import com.wjc.smarthome.dto.system.SysResourceTree;
import com.wjc.smarthome.param.system.SysResourceCreateBean;
import com.wjc.smarthome.param.system.SysResourceUpdateBean;
import com.wjc.smarthome.service.system.SysResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/1--15:25
 */
@RestController
@Api(tags = "资源管理")
@ApiOperation("系统资源的管理")
@RequestMapping("/resource")
@RequiredArgsConstructor
public class SysResourceController{

    private final SysResourceService resourceService;

    @GetMapping("/queryResourceTree")
    @ApiOperation("获取资源树")
    @RequiresPermissions(value = {"resource:query"})
    @SystemLog(module = "资源模块",methods = "获取资源树",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<List<SysResourceTree>> queryResourceTree(){

        return JsonResult.success(resourceService.queryResourceTree());
    }


    @PostMapping("/createResource")
    @ApiOperation("新建资源")
    @RequiresPermissions(value = {"resource:create"},logical = Logical.OR)
    @SystemLog(module = "资源模块",methods = "新建资源",logType = LogType.OPERA,operateType = OperateType.CREATE)
    public JsonResult<Long> createResource(@RequestBody SysResourceCreateBean bean){
        return JsonResult.success(resourceService.createResource(bean));
    }

    @DeleteMapping("/deleteResource")
    @ApiOperation("删除资源")
    @RequiresPermissions(value = {"resource:delete"},logical = Logical.OR)
    @SystemLog(module = "资源模块",methods = "删除资源",logType = LogType.OPERA,operateType = OperateType.DELETE)
    public JsonResult<Boolean> deleteResource(@RequestParam("id") Long id){
        boolean result = resourceService.deleteResource(id);
        if (result){
            return JsonResult.success(true);
        }
        return JsonResult.failure("请先删除此资源的子资源！",false);
    }

    @GetMapping("/queryResourceById")
    @ApiOperation("查找资源")
    @RequiresPermissions(value = {"resource:query"},logical = Logical.OR)
    @SystemLog(module = "资源模块",methods = "查找资源",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<SysResourceDto> queryResourceById(@RequestParam("id") Long id){
        return JsonResult.success(resourceService.queryResourceById(id));
    }

    @PostMapping("/updateResource")
    @ApiOperation("更新资源")
    @RequiresPermissions(value = {"resource:update"},logical = Logical.OR)
    @SystemLog(module = "资源模块",methods = "更新资源",logType = LogType.OPERA,operateType = OperateType.UPDATE)
    public JsonResult<Boolean> updateResource(@RequestBody SysResourceUpdateBean bean){
        return JsonResult.success(resourceService.updateResource(bean));
    }
}
