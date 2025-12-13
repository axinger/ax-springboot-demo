package com.github.axinger.jackson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,      // 使用逻辑名称（如 "dog"）
        include = JsonTypeInfo.As.PROPERTY, // 类型信息作为 JSON 属性
        property = "type"                // JSON 中的字段名
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = "dog"),
        @JsonSubTypes.Type(value = Cat.class, name = "cat")
})
public abstract class Animal {
}
