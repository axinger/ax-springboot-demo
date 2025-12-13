package com.github.axinger.dto;

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
    @com.alibaba.fastjson2.annotation.JSONField(value = true)
    private final String desc;

    // ✅ 根据 code 查找枚举（用于从数据库反序列化）
    public static Gender fromCode(int code) {
        for (Gender gender : Gender.values()) {
            if (gender.code == code) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender code: " + code);
    }

    public static Gender fromDesc(String desc) {
        for (Gender gender : Gender.values()) {
            if (gender.desc.equals(desc)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Invalid gender desc: " + desc);
    }


//    @Override
//    public String toString() {
//        return desc;
//    }

    // ⬇️ 全局注册序列化/反序列化器
//    static {
//        // 在应用启动时调用一次（如 Spring 的 @PostConstruct）
//        com.alibaba.fastjson2.JSON.registerIfAbsent(Gender.class, new GenderObjectWriter());
//        com.alibaba.fastjson2.JSON.registerIfAbsent(Gender.class, new GenderObjectReader());
//    }

}
