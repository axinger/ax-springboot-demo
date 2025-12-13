package com.github.axinger.config;

import io.netty.util.concurrent.FastThreadLocal;

public class MyHolder {

//    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    private static final FastThreadLocal<String> CURRENT_ID = new FastThreadLocal<>();

    public static String getUserId() {
        return CURRENT_ID.get();
    }

    public static void setUserId(String context) {
        CURRENT_ID.set(context);
    }

    public static void removeUserId() {
        CURRENT_ID.remove();
    }
}
