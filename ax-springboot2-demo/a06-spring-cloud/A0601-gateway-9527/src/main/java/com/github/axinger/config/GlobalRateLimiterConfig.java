package com.github.axinger.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class GlobalRateLimiterConfig {

    /**
     * IP限流策略 - 基于客户端IP地址
     */
    @Bean
    @Primary
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
            return Mono.just(ip);
        };
    }

    /**
     * 用户ID限流策略 - 基于用户标识
     */
    @Bean
    public KeyResolver userKeyResolver() {
        return exchange -> {
            // 从请求头或JWT中获取用户ID
            String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
            if (userId == null) {
                userId = "anonymous";
            }
            return Mono.just(userId);
        };
    }

    /**
     * API路径限流策略 - 基于请求路径
     */
    @Bean
    public KeyResolver pathKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

    @Bean
    public KeyResolver ipAndPathKeyResolver() {
        return exchange -> {
            String ip = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
            String path = exchange.getRequest().getPath().value();
            // 清理路径中的动态参数（可选），避免 /user/1 和 /user/2 被视为不同接口
            // 如果你的路径是 /payment/search?id=xxx，则 path 是 /payment/search，已经干净
            return Mono.just(ip + "_" + path);
        };
    }

    /**
     * 自定义Redis限流器 - 全局默认配置
     */
    @Bean("defaultRateLimiter")
    @Primary
    public RedisRateLimiter defaultRateLimiter() {
        // replenishRate: 每秒生成的令牌数
        // burstCapacity: 令牌桶最大容量
//        return new RedisRateLimiter(20, 50);
        return new RedisRateLimiter(1, 1);
        // 每5秒1个，桶容量=1
    }


    /**
     * 严格限流器 - 适用于敏感接口
     */
    @Bean("strictRateLimiter")
    public RedisRateLimiter strictRateLimiter() {
        return new RedisRateLimiter(5, 10);
    }

    /**
     * 宽松限流器 - 适用于查询接口
     */
    @Bean("relaxedRateLimiter")
    public RedisRateLimiter relaxedRateLimiter() {
        return new RedisRateLimiter(50, 100);
    }
}
