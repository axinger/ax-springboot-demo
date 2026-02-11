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

        RateLimitProperties.Rule rule = findMatchingRule(uri);
        if (rule == null) {
            return true; // 未匹配 → 不限流
        }
        String key = "LIMITER:" + ":" + uri + ":" + method + ":" + ip;
        RRateLimiter limiter = redissonClient.getRateLimiter(key);
        boolean wasSet = limiter.trySetRate(RateType.OVERALL, rule.getRate(), rule.getInterval(), RateIntervalUnit.SECONDS);
        if (wasSet) {
            /// 只有首次初始化时才设置 TTL
            redissonClient.getKeys().expire(key, 24, TimeUnit.HOURS);
        }
        return limiter.tryAcquire(1);
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