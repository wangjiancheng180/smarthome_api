package com.wjc.smarthome.shiro.realm;


import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.service.system.SysResourceService;
import com.wjc.smarthome.service.system.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

/**
 * @author 王建成
 * @date 2023/2/22--20:46
 * 这个主要作用户登录认证
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserInfoService userInfoService;

    @Value("${security.salt}")
    private String salt;


    @Autowired
    private SysResourceService sysResourceService;

    /**
     * 这个方法表面realm支持那种token支持UsernamePasswordToken
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UsernamePasswordToken;
    }
    /**
     * 登录认证的具体操作
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //获取用户名
        String username = authenticationToken.getPrincipal().toString();
        UserInfo userInfo = userInfoService.findByUserName(username);

        if (userInfo!=null){
            return new SimpleAuthenticationInfo(username,userInfo.getPassword(), ByteSource.Util.bytes(salt),"userRealm");
        }
        return null;
    }

    /**
     * 添加权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //得到主要身份信息，这里就是user
        String username = (String) principalCollection.getPrimaryPrincipal();
        UserInfo userInfo = userInfoService.findByUserName(username);
        Set<String> resourceKeys = sysResourceService.queryButtonKey(userInfo.getId());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(resourceKeys);
        return authorizationInfo;
    }


}
