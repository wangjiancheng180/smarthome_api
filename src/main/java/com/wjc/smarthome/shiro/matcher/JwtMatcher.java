package com.wjc.smarthome.shiro.matcher;

import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * @author 王建成
 * @date 2023/2/23--20:20
 * 请求里带了token我就直接放行不比较了
 * 判断token是否有效在relam和filter里去做
 */
public class JwtMatcher extends SimpleCredentialsMatcher {

    @Override
    protected boolean equals(Object tokenCredentials, Object accountCredentials) {
        return true;
    }
}
