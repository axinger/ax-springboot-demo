package com.github.axinger.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

public class MyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
//        String property = context.getEnvironment().getProperty("my.custom.property");
//        return "true".equals(property);

        // 确保返回 true 当需要创建 Bean 时
        return context.getEnvironment().getProperty("my.custom1.property", Boolean.class, false);
    }
}
