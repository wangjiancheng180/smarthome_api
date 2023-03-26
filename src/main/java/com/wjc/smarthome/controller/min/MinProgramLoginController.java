package com.wjc.smarthome.controller.min;

import cn.hutool.core.util.StrUtil;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.param.min.RegisterInfoBean;
import com.wjc.smarthome.param.system.LoginInfo;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.service.min.MinProgramService;
import com.wjc.smarthome.shiro.token.PersonToken;
import com.wjc.smarthome.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wjc
 * @Date: 2023-03-20 09:52
 **/
@RestController
@RequestMapping("/min")
@Api(tags = "小程序设备认证")
@RequiredArgsConstructor
public class MinProgramLoginController {

    private final RedisService redisService;
    private final MinProgramService minProgramService;

    @PostMapping("/register")
    @ApiOperation(value = "用户绑定设备")
    public JsonResult<Boolean> register(@RequestBody RegisterInfoBean bean){
       return minProgramService.register(bean);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public JsonResult<String> login(@RequestBody LoginInfo loginInfo, HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken authenticationToken = new PersonToken(loginInfo.getUsername(),loginInfo.getPassword());
        try {
            subject.login(authenticationToken);
            Map<String,String> payloads = new HashMap<>(1);
            payloads.put("username",loginInfo.getUsername());
            String token = TokenUtils.createToken(payloads);
            //缓存token设置10天有效期
            //stringRedisTemplate.opsForValue().set();
            redisService.save(StrUtil.format("token:{}",loginInfo.getUsername()),token,10, TimeUnit.DAYS);
            //下发token
            response.setHeader("Authorization",token);
            //这里必须将下发的头暴露出来，浏览器只能访问以下默认的 响应头
            // Cache-Control，Content-Language，Content-Type，Expires，Last-Modified，Pragma
            //response.addHeader("Access-Control-Expose-Headers","Authorization");
            //这里返回前端需要的用户信息，包括菜单类
            return JsonResult.success(1,"登录成功",token);
        } catch (AuthenticationException e) {
            return JsonResult.failure(3,"用户名或密码错误！",null);
        }
    }

}
