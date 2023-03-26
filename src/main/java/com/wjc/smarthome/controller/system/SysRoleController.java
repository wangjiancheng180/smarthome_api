package com.wjc.smarthome.controller.system;


import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;
import com.wjc.smarthome.controller.BaseController;
import com.wjc.smarthome.dto.system.SysRoleDto;
import com.wjc.smarthome.enetity.system.Role;
import com.wjc.smarthome.param.system.SysRoleCreateBean;
import com.wjc.smarthome.param.system.SysRoleUpdateBean;
import com.wjc.smarthome.service.system.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/12--9:49
 */
@RestController
@Api(tags = "角色管理")
@ApiOperation("角色管理")
@RequestMapping("/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final RoleService roleService;

    @GetMapping("/queryRoleList")
    @ApiOperation("获取角色列表")
    @RequiresPermissions({"role:query"})
    @SystemLog(module = "角色模块",methods = "获取角色列表",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<List<Role>> queryRoleList() {
        return JsonResult.success(roleService.queryRoleList());
    }

    @PostMapping("/createRole")
    @ApiOperation("创建角色")
    @RequiresPermissions({"role:create"})
    @SystemLog(module = "角色模块",methods = "创建角色",logType = LogType.OPERA,operateType = OperateType.CREATE)
    public JsonResult<Long> createRole(@RequestBody SysRoleCreateBean bean) {
        return JsonResult.success(roleService.createRole(bean));
    }

    @DeleteMapping("/deleteRole")
    @ApiOperation("删除角色")
    @RequiresPermissions({"role:delete"})
    @SystemLog(module = "角色模块",methods = "删除角色",logType = LogType.OPERA,operateType = OperateType.DELETE)
    public JsonResult<Boolean> deleteRole(@RequestParam("id") Long id) {
        boolean flag = roleService.deleteRole(id);
        if (flag) {
            return JsonResult.success(true);
        }
        return JsonResult.failure("该角色还有关联的用户无法删除！", false);
    }

    @PostMapping("/updateRole")
    @ApiOperation("更新角色")
    @RequiresPermissions({"role:update"})
    @SystemLog(module = "角色模块",methods = "更新角色",logType = LogType.OPERA,operateType = OperateType.UPDATE)
    public JsonResult<Boolean> updateRole(@RequestBody SysRoleUpdateBean bean) {
        return JsonResult.success(roleService.updateRole(bean));
    }

    @GetMapping("/queryRoleById")
    @ApiOperation("通过id查找角色")
    @RequiresPermissions({"role:query"})
    @SystemLog(module = "角色模块",methods = "查找角色",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<SysRoleDto> queryRoleById(@RequestParam("id") Long id) {
        return JsonResult.success(roleService.queryRoleById(id));

    }

}
