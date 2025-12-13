package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/*
Shape.OBJECT 表示：该字段在 JSON 中应被表示为一个 JSON 对象（即 {}），而不是默认的标量值（如字符串、数字等）。
但它 通常用于枚举（enum）类型，目的是让枚举在 JSON 中以对象形式输出，而不是简单的名称字符串。
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Status {
    ACTIVE("active", "用户活跃"),
    INACTIVE("inactive", "用户不活跃");

    // getter 方法
    private final String code;
    private final String description;

    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
