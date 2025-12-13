package com.github.axinger;

import cn.hutool.core.math.Money;
import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName NumTests.java
 * @description TODO
 * @createTime 2022年07月29日 01:12:00
 */

public class NumberUtilTests {

    @Test
    void test_1() {
        final String s = NumberUtil.decimalFormat("#.##%", 0.123456);
        System.out.println("s = " + s);
    }

    @Test
    void test_2() {
        final String s = NumberUtil.decimalFormat("#%", 0.123456);
        System.out.println("s = " + s);
    }

    @Test
    void test_3() {
        final String s = NumberUtil.decimalFormat("#", 2.123456);
        System.out.println("s = " + s);
    }

    @Test
    void test_4() {
        final String s = NumberUtil.decimalFormat("#", new BigDecimal("2.123456"));
        System.out.println("s = " + s);
    }

    @Test
    void test_5() {
//        final String s = NumberUtil.decimalFormat("#%", 1.235, RoundingMode.CEILING);
        // #.##% =》 以百分比方式计数，并取两位小数
        final String s = NumberUtil.decimalFormat("#.##% ", 0.123456);
        System.out.println("s = " + s);
    }

    @Test
    void test6() {
        String s = NumberUtil.decimalFormat(",###", 123456);
        System.out.println("s = " + s);
    }

    @Test
    void test7() {
        boolean a = NumberUtil.isNumber("a");
        System.out.println("a = " + a);
    }

    @Test
    void test8() {
        //判断两个数字是否相邻，例如1和2相邻，1和3不相邻
        boolean a = NumberUtil.isBeside(1, 3);
        System.out.println("a = " + a);
    }

    @Test
    void test9() {
        //判断两个数字是否相邻，例如1和2相邻，1和3不相邻
        int i = NumberUtil.partValue(5, 3);
        System.out.println("i = " + i);

        int i2 = NumberUtil.partValue(5, 3, false);
        System.out.println("i2 = " + i2);
    }

    @Test
    void test10() {


        Money money = new Money();
        money.setAmount(new BigDecimal(1));

        long cent = money.getCent();
        System.out.println("cent = " + cent);
    }
}
