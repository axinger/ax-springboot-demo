package com.github.axinger.dto;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;

import java.lang.reflect.Type;

public class GenderObjectReader implements ObjectReader<Gender> {
    @Override
    public Gender readObject(JSONReader jsonReader, Type fieldType, Object fieldName, long features) {
        if (jsonReader.readIfNull()) {
            return null;
        }

        Object value = jsonReader.readAny();
        if (value instanceof Integer code) {
            return Gender.fromCode(code);
        } else if (value instanceof String desc) {
            return Gender.fromDesc(desc);
        }
        return Gender.UNKNOWN;
    }
}
