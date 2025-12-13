package com.github.axinger.extensions;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NullSafeExtensions {
    // 安全获取字符串（若为 null 返回空字符串）
    public static String orEmpty(String str) {
        return str != null ? str : "";
    }

    // 安全获取集合（若为 null 返回空集合）
    public static <T> List<T> orEmpty(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }

    // 安全调用方法（避免链式调用 NPE）
    public static <T, R> R safeCall(T target, Function<T, R> mapper, R defaultValue) {
        try {
            return target != null ? mapper.apply(target) : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
