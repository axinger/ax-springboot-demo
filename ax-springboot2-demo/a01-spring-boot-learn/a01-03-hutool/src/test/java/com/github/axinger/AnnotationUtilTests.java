package com.github.axinger;

import cn.hutool.core.annotation.AnnotationUtil;
import org.junit.jupiter.api.Test;

// Retention注解决定MyAnnotation注解的生命周期


public class AnnotationUtilTests {
    @Test
    void test() {

        // value为"测试"
        Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class, "age");
        System.out.println("value = " + value);
    }
}
