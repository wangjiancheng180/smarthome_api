package com.wjc.smarthome.config;

import com.wjc.smarthome.shiro.filter.TokenFilter;
import com.wjc.smarthome.shiro.matcher.JwtMatcher;
import com.wjc.smarthome.shiro.realm.JwtRealm;
import com.wjc.smarthome.shiro.realm.PersonRealm;
import com.wjc.smarthome.shiro.realm.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.DefaultAdvisorChainFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

/**
 * @author 王建成
 * @date 2023/2/22--21:13
 */
@Configuration
public class ShiroConfig {


    @Bean
    public  UserRealm userRealm(){
        return new UserRealm();
    }
    @Bean
    public  JwtRealm jwtRealm(){
        return new JwtRealm();
    }
    @Bean
    public PersonRealm personRealm(){return new PersonRealm();}

    @Bean
    public JwtMatcher jwtMatcher(){return new JwtMatcher();}

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(){
        //创建shiro的安全管理
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //创建shiro的匹配密码的对象
        HashedCredentialsMatcher matcher =new HashedCredentialsMatcher();
        //设置匹配密码时用的算法
        matcher.setHashAlgorithmName("MD5");
        //密码迭代加密3次
        matcher.setHashIterations(3);
        UserRealm userRealm = userRealm();
        JwtRealm jwtRealm = jwtRealm();
        PersonRealm personRealm = personRealm();
        //设置userRealm的密码匹配
        userRealm.setCredentialsMatcher(matcher);
        personRealm.setCredentialsMatcher(matcher);
        //将token的密码匹配换成自己的
        jwtRealm.setCredentialsMatcher(jwtMatcher());
//        jwtRealm.setCredentialsMatcher(matcher);
        //将realm配置到manager使用
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm);
        realms.add(jwtRealm);
        realms.add(personRealm);
        manager.setRealms(realms);
//        manager.setRealm(userRealm);
//        manager.setRealm(jwtRealm);
        //关闭shiro的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);
        return manager;
    }

    /**
     * 将filter注入到shiro中
     * @param manager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager manager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = filterFactoryBean.getFilters();
        filters.put("token",new TokenFilter());
        filterFactoryBean.setFilters(filters);

        Map<String, String> filterRuleMap = new HashMap<>();
        filterRuleMap.put("/swagger-ui.html","anon");
        filterRuleMap.put("/swagger-resources/**","anon");
        filterRuleMap.put("/webjars/**","anon");
        filterRuleMap.put("/v2/**","anon");
        filterRuleMap.put("/ws/**","anon");
        filterRuleMap.put("/doc.html","anon");
        filterRuleMap.put("/auth/login","anon");
        filterRuleMap.put("/min/login","anon");
        filterRuleMap.put("/min/register","anon");
        filterRuleMap.put("/**","token");
        filterFactoryBean.setFilterChainDefinitionMap(filterRuleMap);

        filterFactoryBean.setSecurityManager(manager);
        return filterFactoryBean;
    }


//    /**
//     * 配置Shiro内置过滤器拦截范围
//     * @return
//     */
//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
//        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
//
//        //设置不认证可以访问的资源
//        definition.addPathDefinition("/swagger-ui.html","anon");
//        definition.addPathDefinition("/swagger-resources/**","anon");
//        definition.addPathDefinition("/webjars/**","anon");
//        definition.addPathDefinition("/v2/**","anon");
//        definition.addPathDefinition( "/doc.html","anon");
//        //放行登录接口
//        definition.addPathDefinition("/auth/login","anon");
//        //definition.addPathDefinitions();
//        //definition.addPathDefinition("/my/login","anon");
//        //设置登出过滤器
//        //definition.addPathDefinition("/auth/logout","logout");
//        //设置需要进行登录认证的拦截范围
//        definition.addPathDefinition("/**","token");
//        return  definition;
//    }


}
