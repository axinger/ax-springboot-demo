package com.github.axinger.config;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import lombok.NonNull;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class ForwardAuthFilter implements WebFilter {


    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange serverWebExchange, @NonNull WebFilterChain webFilterChain) {

        ServerHttpRequest serverHttpRequest = serverWebExchange.getRequest();


        String path = serverHttpRequest.getPath().toString();

        // 需要校验权限
        if (!WhitePath.whitePaths.contains(path)) {

            // 判断用户是否有该权限
            if (!StpUtil.hasPermission(path)) {
                throw new NotPermissionException(path);
            }
        }

        return webFilterChain.filter(serverWebExchange);
    }
}
