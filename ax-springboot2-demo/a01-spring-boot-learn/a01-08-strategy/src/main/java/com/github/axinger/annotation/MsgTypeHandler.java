package com.github.axinger.annotation;

import com.github.axinger.entity.MsgTypeEnum;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MsgTypeHandler {

    MsgTypeEnum value();
}
