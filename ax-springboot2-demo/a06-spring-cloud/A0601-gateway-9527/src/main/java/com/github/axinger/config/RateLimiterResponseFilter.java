package com.github.axinger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

/// 流响应过滤器
@Slf4j
@Component
public class RateLimiterResponseFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        // 在响应头中添加限流相关信息
        response.getHeaders().add("X-RateLimit-Timestamp", String.valueOf(Instant.now().toEpochMilli()));

        return chain.filter(exchange).then(
            Mono.fromRunnable(() -> {
                // 可以在这里记录请求日志或统计信息
                log.debug("请求完成: {} - {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI().getPath());
            })
        );
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
