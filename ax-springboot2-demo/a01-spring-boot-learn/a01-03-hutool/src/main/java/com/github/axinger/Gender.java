package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Getter;

//定义枚举
@Getter
@AllArgsConstructor
public enum Gender {
    MALE(11, "男性"),
    FEMALE(12, "女性"),
    UNKNOWN(13, "未知");

    private final int code;
    private final String alias;

    @Override
    public String toString() {
        return alias;
    }
}
