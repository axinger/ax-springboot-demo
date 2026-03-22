package com.github.axinger.util;

import com.alibaba.ttl.TransmittableThreadLocal;

public class UserContext {
    // 定义 TTL 变量
    private static final TransmittableThreadLocal<String> USER_ID = new TransmittableThreadLocal<>();

    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    public static String getUserId() {
        return USER_ID.get();
    }

    public static void remove() {
        USER_ID.remove();
    }
}
