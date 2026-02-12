package com.github.axinger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private boolean enabled = true;
    private long defaultRate = 1;
    private long defaultInterval = 1;
    private long defaultWaitTimeoutSeconds = 0;
    private List<Rule> rules = new ArrayList<>();

    @Data
    public static class Rule {
        private String path;
        private long rate;
        private long interval; // 单位：秒
        private long waitTimeoutSeconds; // 单位：秒
    }
}