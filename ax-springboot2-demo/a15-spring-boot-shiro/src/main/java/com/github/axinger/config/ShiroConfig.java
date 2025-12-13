package com.github.axinger.config;


import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * shiro配置
 */
@Configuration
public class ShiroConfig {


    //    //1. 创建安全管理器
    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 给安全管理器设置realm
        manager.setRealm(realm);
        return manager;
    }


    @Bean
    public Realm realm() {
        CustomerRealm customerRealm = new CustomerRealm();
        return customerRealm;
    }


    // 2. 创建过滤器
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 给filter设置安全管理器
        bean.setSecurityManager(securityManager);
        // 配置用户登录请求,需要登录的转发
        bean.setLoginUrl("/");
        // 登录成功后
        bean.setSuccessUrl("/success");
        // 没有权限请求
        bean.setUnauthorizedUrl("/noPermission");

        // 权限拦截规则
        Map<String, String> map = new HashMap<>(16);
        // 登录请求不需要认证
        map.put("/login", "anon");
        // 登录请求,会清空内存
        map.put("/logout", "logout");
//        // admin 开头请求 需要登录
//        map.put("/admin/**", "authc");
//        // user 开头请求 需要登录
//        map.put("/user/**", "authc");

        // 所有请求需要认证,必须写在最后
//        map.put("/**", "authc");

        // 设置规则
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }


//    /**
//     *
//     * 启用shrio授权注解拦截方式，需要借助spring的AOP实现
//     * @return
//     */
//    @Bean
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
//        creator.setProxyTargetClass(true);
//        return creator;
//    }
//
//
//    /**
//     * 启用shrio授权注解拦截方式，AOP式方法级权限检查
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
//        AuthorizationAttributeSourceAdvisor advisor =
//                new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }

}
