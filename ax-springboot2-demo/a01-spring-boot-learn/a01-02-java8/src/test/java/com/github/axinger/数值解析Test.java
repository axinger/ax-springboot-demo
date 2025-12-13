package com.github.axinger;

import org.junit.jupiter.api.Test;

public class 数值解析Test {

    @Test
    void test1() {

//        long a = Integer.parseInt(""); //解析失败报错
//        System.out.println("parseLong = " + a);

        //获取系统属性,不是解析
        Integer integer = Integer.getInteger("a"); //解析失败为null
        System.out.println("integer = " + integer);

        Integer integer2 = Integer.getInteger(null); //解析失败为null
        System.out.println("integer2 = " + integer2);
    }


    //parse*方法比valueOf方法 效率高 。
    @Test
    void test2() {
        // valueOf 调用就是parseLong,会报错
        int a = Integer.parseInt("a");
        System.out.println("valueOf = " + a);
    }

    @Test
    void test3() {
        // valueOf 调用就是parseLong,会报错
        int a = Integer.valueOf("a");
        System.out.println("valueOf = " + a);
    }
}
