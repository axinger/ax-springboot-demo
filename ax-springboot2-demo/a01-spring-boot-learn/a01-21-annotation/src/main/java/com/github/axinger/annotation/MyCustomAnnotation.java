package com.github.axinger.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE}) // 可用在方法或类上
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Documented
public @interface MyCustomAnnotation {
    String value() default ""; // 可以添加属性

    String num() default "0";
}
