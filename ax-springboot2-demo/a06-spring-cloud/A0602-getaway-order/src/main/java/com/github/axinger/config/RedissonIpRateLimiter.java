package com.github.axinger.config;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedissonIpRateLimiter {

    private final RedissonClient redissonClient;
    private final RateLimitProperties properties;
    private final PathMatcher pathMatcher = new AntPathMatcher();

    public boolean allowRequest(String method, String uri, String ip) {
        if (!properties.isEnabled()) {
            return true;
        }

        /// 1. 尝试匹配自定义规则
        RateLimitProperties.Rule rule = findMatchingRule(uri);
        /// 2. 确定最终限流参数
        long rate;
        long interval;
        long waitTimeoutSeconds;
        if (rule != null) {
            rate = rule.getRate();
            interval = rule.getInterval();
            waitTimeoutSeconds = rule.getWaitTimeoutSeconds();
        } else {
            // 👇 使用默认兜底规则
            rate = properties.getDefaultRate();
            interval = properties.getDefaultInterval();
            waitTimeoutSeconds = properties.getDefaultWaitTimeoutSeconds();
        }
        /// 3. 执行限流
        String key = String.format("LIMITER:%s:%s:%s", uri, method, ip);
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        boolean wasSet = limiter.trySetRate(RateType.OVERALL, rate, interval, RateIntervalUnit.SECONDS);
        if (wasSet) {
            /// 只有首次初始化时才设置 TTL
            redissonClient.getKeys().expire(key, 24, TimeUnit.HOURS);
        }
        try {
            /// ⏳ 阻塞等待最多 waitTimeoutSeconds 秒，直到获取到令牌
            return limiter.tryAcquire(1, waitTimeoutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private RateLimitProperties.Rule findMatchingRule(String uri) {
        for (RateLimitProperties.Rule rule : properties.getRules()) {
            if (pathMatcher.match(rule.getPath(), uri)) {
                return rule;
            }
        }
        return null;
    }
}