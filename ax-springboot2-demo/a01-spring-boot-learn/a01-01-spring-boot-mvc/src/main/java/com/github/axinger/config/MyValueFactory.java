package com.github.axinger.config;


//public final class MyValueFactory {
//
//    private static final String PREFIX = "/test";
//
//    // 私有构造，防止实例化
//    private MyValueFactory() {
//        throw new UnsupportedOperationException("Utility class");
//    }
//    public static String path(String flag) {
//        return flag + ".do";
//    }
//
//
//}

public interface MyValueFactory {
    static String path(String flag) {
        return flag + ".do";
    }

}
