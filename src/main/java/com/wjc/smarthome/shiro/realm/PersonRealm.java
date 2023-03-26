package com.wjc.smarthome.shiro.realm;

import com.wjc.smarthome.enetity.iot.Person;
import com.wjc.smarthome.service.iot.PersonService;
import com.wjc.smarthome.shiro.token.JwtToken;
import com.wjc.smarthome.shiro.token.PersonToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;

/**
 * @Author: wjc
 * @Date: 2023-03-26 14:54
 **/
public class PersonRealm extends AuthorizingRealm {

    @Autowired
    private PersonService personService;

    @Value("${security.salt}")
    private String salt;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof PersonToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        Person person = personService.getByUserName(username);
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo();
        Optional.ofNullable(person).ifPresent((p)->{
            simpleAuthenticationInfo.setPrincipals(new SimplePrincipalCollection(p.getUsername(),p.getPhone()));
            simpleAuthenticationInfo.setCredentials(p.getPassword());
            simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(salt));
        });
        return simpleAuthenticationInfo;
    }
}
