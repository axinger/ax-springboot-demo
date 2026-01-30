package com.github.axinger.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/monitor/rate-limit")
@RequiredArgsConstructor
public class RateLimitController {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    /**
     * 获取限流统计信息
     */
    @GetMapping("/stats")
    public Mono<Map<String, Object>> getRateLimitStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("timestamp", System.currentTimeMillis());
        stats.put("status", "running");


        // 使用ping命令测试Redis连接
        return redisTemplate.getConnectionFactory()
                .getReactiveConnection()
                .ping()
                .map(response -> {
                    stats.put("redisConnected", true);
                    stats.put("redisPingResponse", response);
                    return stats;
                })
                .onErrorResume(throwable -> {
                    log.warn("Redis连接测试失败: {}", throwable.getMessage());
                    stats.put("redisConnected", false);
                    stats.put("error", throwable.getMessage());
                    return Mono.just(stats);
                })
                .defaultIfEmpty(stats);
    }

    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "gateway-rate-limiter");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
}
