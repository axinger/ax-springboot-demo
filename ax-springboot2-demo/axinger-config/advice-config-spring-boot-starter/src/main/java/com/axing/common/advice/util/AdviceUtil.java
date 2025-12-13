package com.axing.common.advice.util;

import com.axing.common.advice.annotation.IgnoreResponseAdvice;
import com.axing.common.advice.bean.AdviceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import java.util.Objects;

@Slf4j
public class AdviceUtil {

    public static Boolean filter(AdviceProperties adviceProperties, MethodParameter methodParameter) {

        log.debug("filterPackageAllSet = {}", adviceProperties.filterPackageAllSet());

        Class<?> declaringClass = methodParameter.getDeclaringClass();
        // 检查过滤包路径
        long count = adviceProperties.filterPackageAllSet().stream()
                .filter(val -> declaringClass.getName().contains(val)).count();
        if (count > 0) {
            return false;
        }
        // 检查<类>过滤列表
        if (adviceProperties.getFilterClass().contains(declaringClass.getName())) {
            return false;
        }
        // 检查注解是否存在
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return true;
    }
}
