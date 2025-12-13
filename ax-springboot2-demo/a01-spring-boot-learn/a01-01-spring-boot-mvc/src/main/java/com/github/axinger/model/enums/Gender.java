package com.github.axinger.model.enums;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Gender {

    none(0, "保密"),
    male(1, "男性"),
    female(2, "女性");


    private int code;

    private String alias;

    //自定义反序列化: 用于指定在 JSON 反序列化过程中，如何将 JSON 数据转换为 Java 对象
    //构造器/工厂方法标记: 标记一个构造函数或静态工厂方法，使其能够被 Jackson 用于创建对象实例
    @JsonCreator
    public static Gender fromCode(int code) {

//        return Arrays.stream(Gender.values())
//                .filter(s -> s.code == code)
//                .findFirst()
//                .orElse(none);

        return EnumUtil.getBy(Gender::getCode, code, Gender.none);
    }

    //使用@JsonCreator和@JsonValue注解来自定义序列化和反序列化的方式。
    // 这样可以在枚举类中指定一个方法，用来将传入的字符串转换为对应的枚举值
    @JsonValue
    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return alias;
    }
}
