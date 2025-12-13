package com.github.axinger.config;

import org.springframework.core.env.PropertySource;

import java.util.Random;

public class CustomPropertySource extends PropertySource<Random> {

    public CustomPropertySource() {
        super("customPropertySource", new Random());
    }

    @Override
    public Object getProperty(String name) {
        if (!name.startsWith("custom.")) {
            return null;
        }

        if (name.equals("custom.timestamp")) {
            return System.currentTimeMillis();
        }

        if (name.equals("custom.randomString")) {
            // 生成随机字符串的逻辑
            return generateRandomString();
        }

        // 添加更多自定义属性...
        return null;
    }

    private String generateRandomString() {
        // 实现你的随机字符串生成逻辑
        return "RAND_" + System.currentTimeMillis();
    }
}
