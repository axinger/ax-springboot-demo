package com.github.axinger.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        Map<String, Object> customProperties = new HashMap<>();
        customProperties.put("custom2.timestamp", System.currentTimeMillis());
        customProperties.put("custom2.randomString", generateRandomString());

        environment.getPropertySources().addFirst(
                new MapPropertySource("customProperties", customProperties)
        );

    }

    private String generateRandomString() {
        // 实现你的随机字符串生成逻辑
        return "RAND_" + System.currentTimeMillis();
    }
}
