package com.github.axinger.dto;

import com.alibaba.fastjson2.annotation.JSONField;

public enum Type implements BaseEnum {
    X(101, "Big"),
    M(102, "Medium"),
    S(103, "Small");

    private final int code;
    private final String name;

    Type(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    @JSONField(value = true)
    public String getCode() {
        return String.valueOf(code);
    }

    @Override
    public String getDesc() {
        return name;
    }

}
