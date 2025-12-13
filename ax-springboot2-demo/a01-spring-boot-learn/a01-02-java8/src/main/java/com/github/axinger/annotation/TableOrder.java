package com.github.axinger.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableOrder {

    int index() default -1;

    DateType dateType();
}
