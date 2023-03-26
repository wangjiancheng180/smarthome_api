package com.wjc.smarthome.shiro.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author: wjc
 * @Date: 2023-03-26 14:56
 **/
@Data
@AllArgsConstructor
public class PersonToken implements AuthenticationToken {

    private String username;

    private String password;

    @Override
    public Object getPrincipal() {
        return this.username;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }
}
