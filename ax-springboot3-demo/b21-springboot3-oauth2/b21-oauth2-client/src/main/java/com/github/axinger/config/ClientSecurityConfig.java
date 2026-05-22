package com.github.axinger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 客户端安全配置
 * 配置OAuth2登录和页面访问权限
 */
@Configuration
@EnableWebSecurity
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // 放行静态资源和首页
                        .requestMatchers("/", "/index", "/assets/**", "/webjars/**", "/error").permitAll()
                        // 用户相关页面需要登录
                        .requestMatchers("/user/**", "/messages/**").authenticated()
                        .anyRequest().authenticated()
                )
                // 配置OAuth2登录
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/messaging-client-oidc")
                        .defaultSuccessUrl("/", true)
                )
                // 配置登出
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
