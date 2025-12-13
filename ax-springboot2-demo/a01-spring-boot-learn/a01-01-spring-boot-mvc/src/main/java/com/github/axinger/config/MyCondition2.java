package com.github.axinger.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

public class MyCondition2 implements Condition {
    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
//        String property = context.getEnvironment().getProperty("my.custom2.property");
//        return "true".equals(property);

        return context.getEnvironment().getProperty("my.custom2.property", Boolean.class, false);
//        SpringUtil.getProperty("my.custom2.property", Boolean.class, false);
    }
}
