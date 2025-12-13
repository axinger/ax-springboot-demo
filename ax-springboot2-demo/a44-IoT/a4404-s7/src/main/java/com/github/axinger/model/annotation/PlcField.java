package com.github.axinger.model.annotation;

import com.github.axinger.model.enums.PlcDataType;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 格式 "%DB1.DBW150:WORD"
 */
public @interface PlcField {

    String offset();

    PlcDataType dataType();
}
