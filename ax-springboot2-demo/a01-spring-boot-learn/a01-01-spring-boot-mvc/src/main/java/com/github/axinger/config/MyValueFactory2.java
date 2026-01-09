package com.github.axinger.config;


import org.springframework.stereotype.Component;

/*
 * springboot方式 @myValueFactory2.path 取值
 */
@Component
public final class MyValueFactory2 {


    public static String path(String flag) {
        return flag + ".do2";
    }
}
