package com.wjc.smarthome.shiro.realm;


import cn.hutool.core.util.StrUtil;
import com.wjc.smarthome.enetity.system.UserInfo;
import com.wjc.smarthome.service.system.SysResourceService;
import com.wjc.smarthome.service.system.UserInfoService;
import com.wjc.smarthome.service.RedisService;
import com.wjc.smarthome.util.JwtToken;
import com.wjc.smarthome.util.TokenUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Set;

/**
 * @author 王建成
 * @date 2023/2/23--14:56
 * 进行token登录认证
 */
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SysResourceService sysResourceService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    /**
     * 授权
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

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getPrincipal();
        if (!TokenUtils.verify(token)){
            //token有效性验证失败
            return null;
        }
        //取得username
        String username = (String)TokenUtils.parseToken(token, "username");
       // Object cacheToken = redisTemplate.opsForValue().get(StrUtil.format("token:{}",username));
        String cacheToken = redisService.get(StrUtil.format("token:{}", username));
        //这里不进行token的对比也是为了将不同业务的登录一起处理，redis里存储的token是最新登录的token，
        //token自身有过期时间，上面验证，token的合法性也在上面验证了
        if (StrUtil.isBlank(cacheToken)){
            //TODO:拓展强制T下线
            return null;
        }

        return new SimpleAuthenticationInfo(username,"","jwtRealm");
    }
}
