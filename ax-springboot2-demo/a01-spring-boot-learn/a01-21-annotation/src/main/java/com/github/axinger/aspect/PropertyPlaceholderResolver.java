package com.github.axinger.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

@Component
public class PropertyPlaceholderResolver {

    private final PropertyPlaceholderHelper placeholderHelper;
    private final Environment environment;

    @Autowired
    public PropertyPlaceholderResolver(Environment environment) {
        this.environment = environment;
        // 定义占位符解析的前缀和后缀
        this.placeholderHelper = new PropertyPlaceholderHelper("${", "}", ":", true);
    }

    /**
     * 解析占位符表达式，支持默认值
     *
     * @param expression 包含占位符的字符串
     * @return 解析后的值
     */
    public String resolvePlaceholder(String expression) {
        return placeholderHelper.replacePlaceholders(expression, placeholderName -> {
            String[] parts = placeholderName.split(":", 2);
            String key = parts[0];
            String defaultValue = parts.length > 1 ? parts[1] : null;

            // 从 Environment 获取属性值，没有则使用默认值
            return environment.getProperty(key, defaultValue);
        });
    }
}
