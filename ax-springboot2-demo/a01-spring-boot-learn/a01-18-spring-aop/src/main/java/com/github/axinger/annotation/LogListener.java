package com.github.axinger.annotation;

import java.lang.annotation.*;

//@Target({ElementType.METHOD, ElementType.TYPE}) // 可用在方法或类上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogListener {

    String value() default "";
}
