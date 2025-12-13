package com.github.axinger.jackson;

import cn.hutool.core.util.EnumUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SysGender {
    /// 在反序列化枚举时，当 JSON 值无法匹配任何枚举常量时，指定一个默认值。
    @JsonEnumDefaultValue
    UNKNOWN(0, "未知"),
    FEMALE(1, "女"),
    MALE(2, "男"),
    ;

    /// 序列化为单个值
    @JsonValue
    private final int code;

    private final String desc;


//    @JsonValue  // 序列化时使用
//    public int getCode() {
//        return code;
//    }


    /// 自定义反序列化构造,需要静态方法
//    @JsonCreator
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SysGender fromCode(int code) {
        return EnumUtil.getBy(SysGender::getCode, code, SysGender.UNKNOWN);
    }

    // 添加接受字符串的 JsonCreator
//    @JsonCreator
//    public static SysGender fromValue(String value) {
//        // 根据实际需求实现字符串到枚举的转换逻辑
//        return Enum.valueOf(SysGender.class, value);
//    }
}
