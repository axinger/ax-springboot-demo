package com.github.axinger.dto;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Gender {
    UNKNOWN(0, "未知"),
    FEMALE(1, "女"),
    MALE(2, "男"),
    ;
    private final int code;

    /// 枚举值
    @com.alibaba.fastjson2.annotation.JSONField(value = true)
    private final String desc;

    // ✅ 根据 code 查找枚举（用于从数据库反序列化）
//    public static Gender fromCode(int code) {
//        for (Gender gender : Gender.values()) {
//            if (gender.code == code) {
//                return gender;
//            }
//        }
//        throw new IllegalArgumentException("Invalid gender code: " + code);
//    }

    //自定义反序列化: 用于指定在 JSON 反序列化过程中，如何将 JSON 数据转换为 Java 对象
    //构造器/工厂方法标记: 标记一个构造函数或静态工厂方法，使其能够被 Jackson 用于创建对象实例
    @JsonCreator
    public static Gender fromCode(int code) {
//        return Arrays.stream(Gender.values())
//                .filter(s -> s.code == code)
//                .findFirst()
//                .orElse(none);
        return EnumUtil.getBy(Gender::getCode, code, Gender.UNKNOWN);
    }

    public static Gender fromDesc(String desc) {
        for (Gender gender : Gender.values()) {
            if (gender.desc.equals(desc)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender desc: " + desc);
    }


}
