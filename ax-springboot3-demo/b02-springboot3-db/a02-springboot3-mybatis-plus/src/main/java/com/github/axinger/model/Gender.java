package com.github.axinger.model;

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
    private int code;
    private String description;

    public static void main(String[] args) {
        // 枚举字面值
        System.out.println("Gender.unknown.name() = " + Gender.unknown.name());
        System.out.println("Gender.valueOf = " + Gender.valueOf("female"));
        System.out.println("Gender.female.ordinal() = " + Gender.female.ordinal());
        System.out.println("Gender.female.getDeclaringClass() = " + Gender.female.getDeclaringClass());
    }

    @Override
    public String toString() {
        return this.description;
    }
}
