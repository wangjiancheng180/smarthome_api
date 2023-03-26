package com.wjc.smarthome.controller.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;
import com.wjc.smarthome.controller.BaseController;
import com.wjc.smarthome.dto.system.AuthInfo;
import com.wjc.smarthome.dto.system.UserInfoDto;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.param.system.UserInfoCreateBean;
import com.wjc.smarthome.param.system.UserInfoQueryBean;
import com.wjc.smarthome.service.system.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 王建成
 * @date 2022/4/17--22:36
 */
@Api(tags = "用户管理")
@ApiOperation("用户管理")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserInfoController{

    private final UserInfoService userInfoService;

    @PostMapping("/page")
    @ApiOperation("分页查询用户")
    @RequiresPermissions({"user:query"})
    @SystemLog(module = "用户模块",methods = "分页获取用户",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<IPage<UserInfo>> pageList(@RequestBody UserInfoQueryBean bean){
        return JsonResult.success(userInfoService.pageList(bean));
    }

    @GetMapping("/queryByUsername")
    @ApiOperation("根据用户名查找")
//    @RequiresPermissions({"user:query"})
    @SystemLog(module = "用户模块",methods = "用户名查找用户",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<AuthInfo> queryByUsername(@RequestParam String username){
        return JsonResult.success(userInfoService.queryByUsername(username));
    }

    @GetMapping("/queryUserInfo")
    @ApiOperation("用户列表")
    @RequiresPermissions({"user:query"})
    @SystemLog(module = "用户模块",methods = "用户列表",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<List<UserInfoDto>> queryUserInfo(){
        return JsonResult.success(userInfoService.queryUserInfo());
    }

    @PostMapping("/createUserInfo")
    @ApiOperation("创建用户")
    @RequiresPermissions({"user:create"})
    @SystemLog(module = "用户模块",methods = "新增用户",logType = LogType.OPERA,operateType = OperateType.CREATE)
    public JsonResult<Boolean> createUserInfo(@RequestBody UserInfoCreateBean bean){
        boolean flag = userInfoService.createUserInfo(bean);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("用户名或手机号重复！",false);
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation("更新用户")
    @RequiresPermissions({"user:update"})
    @SystemLog(module = "用户模块",methods = "更新用户",logType = LogType.OPERA,operateType = OperateType.UPDATE)
    public JsonResult<Boolean> updateUserInfo(@RequestBody UserInfoCreateBean bean){
        boolean flag = userInfoService.updateUserInfo(bean);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该用户或用户名手机号重复！",false);
    }

    @DeleteMapping("/deleteUserInfo")
    @ApiOperation("删除用户")
    @RequiresPermissions({"user:delete"})
    @SystemLog(module = "用户模块",methods = "删除用户",logType = LogType.OPERA,operateType = OperateType.DELETE)
    public JsonResult<Boolean> deleteUserInfo(@RequestParam("id") Long id){
        boolean flag = userInfoService.deleteUserInfo(id);
        if (flag){
            return JsonResult.success(true);
        }
        return JsonResult.failure("不存在该用户！",false);
    }

    @GetMapping("/queryUserById")
    @ApiOperation("通过id查询用户")
    @SystemLog(module = "用户模块",methods = "通过id查询用户",logType = LogType.OPERA,operateType = OperateType.QUERY)
    public JsonResult<UserInfoDto> querUserById(@RequestParam("id") Long id){
        return JsonResult.success(userInfoService.queryUserById(id));
    }

}
