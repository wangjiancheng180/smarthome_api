package com.wjc.smarthome.shiro.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wjc.smarthome.common.JsonResult;
import com.wjc.smarthome.shiro.token.JwtToken;
import com.wjc.smarthome.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author 王建成
 * @date 2023/2/23--12:33
 */
@Slf4j
public class TokenFilter extends BasicHttpAuthenticationFilter {

    /**
     * 这里验证失败 会进入onAccessDenied
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        //放行option预检请求
        if(RequestMethod.OPTIONS.equals(req.getMethod())){
            return true;
        }
        String token = req.getHeader(TokenUtils.header);
        //验证token
        if (StrUtil.isBlank(token)){
            //请求头里不带token不放行
            return false;
        }
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new JwtToken(token));
            return true;
        }catch ( AuthenticationException e){
            log.error("非法token:{}",e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse response) throws Exception {
        //验证失败
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(JsonResult.failure(4,"请重新登录！",false)));
        writer.flush();
        writer.close();
        return false;
    }


    /**
     * 登陆失败
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        return false;
    }

}
