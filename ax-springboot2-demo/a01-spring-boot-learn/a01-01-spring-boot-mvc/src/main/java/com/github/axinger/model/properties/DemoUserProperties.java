package com.github.axinger.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * record 类 ConfigurationProperties 绑定演示
 * 展示 record + 嵌套 record 的绑定能力
 */
@ConfigurationProperties(prefix = "demo.user")
public record DemoUserProperties(String username, String password, Dog dog) {

    public String all() {
        return username + password;
    }

    public record range(int min, int max) {
    }

    public record Dog(int min, int max) {
    }
}
