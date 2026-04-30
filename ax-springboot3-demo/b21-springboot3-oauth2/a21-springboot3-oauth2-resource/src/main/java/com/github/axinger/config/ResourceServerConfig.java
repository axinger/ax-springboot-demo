package com.github.axinger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 资源服务器安全配置
 * 配置JWT令牌验证和方法级权限控制
 * 在 allinone 单体模式下不加载，由授权服务器的安全配置统一处理
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Profile("!allinone")
public class ResourceServerConfig {

    /**
     * 配置资源服务器的安全过滤器链
     * 所有 /api/** 接口需要有效的JWT访问令牌
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 无状态会话，不创建Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 禁用CSRF（REST API无状态）
                .csrf(csrf -> csrf.disable())
                // 配置请求授权规则
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 配置OAuth2资源服务器，使用JWT令牌验证
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                // 使用授权服务器的JWKS端点获取公钥验证JWT签名
                                .jwkSetUri("http://localhost:9000/oauth2/jwks")
                        )
                );

        return http.build();
    }
}
