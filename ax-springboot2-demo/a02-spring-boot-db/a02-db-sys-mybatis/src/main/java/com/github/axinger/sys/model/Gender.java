package com.github.axinger.sys.model;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    unknown(0, "未知"),
    male(1, "男性"),
    female(2, "女性");

    /**
     * mp m枚举注解,用于存数据库的字段,
     * 没有主键,默认使用 字面值
     */
    @EnumValue
    private final int code;
    private final String description;
}
