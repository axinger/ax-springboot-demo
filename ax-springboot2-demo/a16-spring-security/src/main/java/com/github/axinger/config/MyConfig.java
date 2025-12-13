package com.github.axinger.config;

import com.github.axinger.handler.MyAccessDeniedHandler;
import com.github.axinger.handler.MyAuthenticationEntryPoint;
import com.github.axinger.handler.MyOncePerRequestFilter;
import com.github.axinger.handler.MyPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级别安全性
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class MyConfig {

    @Autowired
    private MyOncePerRequestFilter myOncePerRequestFilter;

    @Autowired
    private MyPermissionEvaluator myPermissionEvaluator;
//    @Autowired
//    private MyAccessDecisionVoter myAccessDecisionVoter;

    @Bean
    public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(myPermissionEvaluator);
        return handler;
    }

//    @Bean
//    public AccessDecisionManager customAccessDecisionManager() {
//        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
//        decisionVoters.add(myAccessDecisionVoter);
//        return new UnanimousBased(decisionVoters);  // 使用投票机制
//    }

//    @Bean
//    public AuthorizationManager authorizationManager() {
//
//
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    // 将自定义JWT校验过滤链方法UsernamePasswordAuthenticationToken过滤链之前
                .addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http
                .cors()//新加入
                .and()
                .csrf().disable() // 取消跨站请求伪造防护
//                .authorizeRequests()
                .authorizeHttpRequests()
                //"/login"不进行权限验证
//                .antMatchers("/login").permitAll()
//                .antMatchers("/favicon.ico").permitAll()
                .anyRequest()
                .authenticated()   //其他的需要登陆后才能访问
//                .accessDecisionManager(customAccessDecisionManager())  // 自定义权限决策管理器
                .and()
                .formLogin()
                .disable()  // 禁用默认的登录表单
                .logout()
                .disable()
                // 异常处理配置
                .exceptionHandling()
                // 自定义未认证处理逻辑
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                // 自定义无权限处理逻辑
                .accessDeniedHandler(new MyAccessDeniedHandler());
        ;
        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 使用 BCrypt 密码加密
    }

}


//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http    // 将自定义JWT校验过滤链方法UsernamePasswordAuthenticationToken过滤链之前
//            .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class);
//    http
//            .cors()//新加入
//            .and()
//            .csrf().disable() // 取消跨站请求伪造防护
//            .authorizeRequests()
//            //"/login"不进行权限验证
//            .antMatchers("/login").permitAll()
//            .antMatchers("/favicon.ico").permitAll()
//            .anyRequest().authenticated()   //其他的需要登陆后才能访问
//            .and()
//            .formLogin()
//            .disable()  // 禁用默认的登录表单
/// /                .loginPage("/login")  // 自定义登录页面
/// /                //loginProcessingUrl用于指定前后端分离的时候调用后台登录接口的名称
/// ///                .loginPage(null)
/// /                .loginProcessingUrl("/login")
/// /                .usernameParameter("username") // 前端传递的用户名参数名称
/// /                .passwordParameter("password") // 前端传递的密码参数名称
/// /
/// /                //配置登录成功的自定义处理类
/// /                .successHandler(furyAuthSuccessHandler)
/// /                //配置登录失败的自定义处理类
/// /                .failureHandler(furyAuthFailureHandler)
/// /                .and()
//            //loginProcessingUrl用于指定前后端分离的时候调用后台注销接口的名称
//            .logout()
//            .disable()
/// /                .logoutUrl("/logout")
/// /                .logoutSuccessHandler(myLogoutSuccessHandler)
/// /                .and()
//
//
//            //配置没有权限的自定义处理类
//            .exceptionHandling()
//            .accessDeniedHandler(restAuthAccessDeniedHandler)
//            .and();
//    return http.build();
//
//}
