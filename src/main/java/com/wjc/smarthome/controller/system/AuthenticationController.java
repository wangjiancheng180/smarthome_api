package com.wjc.smarthome.controller.system;

import cn.hutool.core.util.StrUtil;
import com.wjc.smarthome.annotation.SystemLog;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.common.LogType;
import com.wjc.smarthome.common.OperateType;
import com.wjc.smarthome.dto.system.AuthInfo;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.param.system.LoginInfo;
import com.wjc.smarthome.service.system.UserInfoService;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 王建成
 * @date 2023/2/22--22:01
 */
@Api(tags = "权限登录")
@ApiOperation("权限登录")
@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final RedisService redisService;

    private final UserInfoService userInfoService;

    @PostMapping("/login")
    @ApiOperation("登录")
    @SystemLog(module = "登录模块",methods = "用户登录",logType = LogType.LOGIN,operateType = OperateType.QUERY)
    public JsonResult<AuthInfo> login(@RequestBody LoginInfo loginInfo, HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken authenticationToken = new UsernamePasswordToken(loginInfo.getUsername(),loginInfo.getPassword());
        try {
            subject.login(authenticationToken);
            Map<String,String> payloads = new HashMap<>();
            payloads.put("username",loginInfo.getUsername());
            String token = TokenUtils.createToken(payloads);
            //缓存token设置10天有效期
            //stringRedisTemplate.opsForValue().set();
            redisService.save(StrUtil.format("token:{}",loginInfo.getUsername()),token,10,TimeUnit.DAYS);
            //下发token
            response.setHeader("Authorization",token);
            //这里必须将下发的头暴露出来，浏览器只能访问以下默认的 响应头
            // Cache-Control，Content-Language，Content-Type，Expires，Last-Modified，Pragma
            //response.addHeader("Access-Control-Expose-Headers","Authorization");
            //这里返回前端需要的用户信息，包括菜单类
            AuthInfo authInfo = userInfoService.queryByUsername(loginInfo.getUsername());
            return JsonResult.success(1,"登录成功",authInfo);
        } catch (AuthenticationException e) {
            return JsonResult.failure(3,"用户名或密码错误！",null);
        }
    }

    @GetMapping("/logout")
    @ApiOperation("登出")
    public JsonResult<Boolean> logout(){
        Subject subject = SecurityUtils.getSubject();
        String username = (String) subject.getPrincipal();
        redisService.delete("user:auth",username);
        UserInfo userInfo = redisService.get("user:userinfo", username, UserInfo.class);
        redisService.delete("user:resource:button",String.valueOf(userInfo.getId()));
        return JsonResult.success();
    }


}
