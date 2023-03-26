package com.wjc.smarthome.util;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author 王建成
 * @date 2023/2/23--14:33
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    public JwtToken(String token) {
        this.token = token;
    }

    public JwtToken() {
    }
}
