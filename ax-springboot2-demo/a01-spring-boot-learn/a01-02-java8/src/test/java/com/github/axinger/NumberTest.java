package com.github.axinger;

import cn.hutool.core.util.NumberUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * @author xing
 * @version 1.0.0
 * @ClassName NumberTest.java
 * @description TODO
 * @createTime 2022年05月20日 22:00:00
 */


public class NumberTest {

    @Test
    void test1() {
        FormatDemo formatDemo = new FormatDemo();// 格式化对象的类
        formatDemo.format1("###,###.###", 123468.4986);
        formatDemo.format1("000,000.000", 18956.6615);
        formatDemo.format1("###,###.###￥", 156498.948165);
        formatDemo.format1("000,000.000￥", 49861.49861);
        formatDemo.format1("##.###%", 0.1656498);
        formatDemo.format1("00.###%", 0.0156165);
        formatDemo.format1("###,###", 0.1684489);
    }

    @Test
    void test2() {
//        float num= (float)6/3;
//        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
//        String s = df.format(num);//返回的是String类型
//        System.out.println("s = " + s);


        final DecimalFormat decimalFormat = new DecimalFormat("0.0000");

        System.out.println(decimalFormat.format(20.0 / 2));

        final DecimalFormat df2 = new DecimalFormat("0.00");
        System.out.println("df2 = " + df2.format(((float) 1 / 8) * 100));

        BigDecimal percentage = BigDecimal.valueOf(1)
                .divide(BigDecimal.valueOf(8), 4, RoundingMode.FLOOR)
                .multiply(BigDecimal.valueOf(100));
        System.out.println("percentage = " + percentage);


    }

    @Test
    void test3() {

        BigDecimal bigDecimal = BigDecimal.valueOf(2.1234);

        System.out.println("bigDecimal = " + bigDecimal);

        System.out.printf("%.1f%n", bigDecimal);

    }

    @Test
    void test4() {

        System.out.println("m*(2的n次方）====================");
        // m<<n: 等于m*(2的n次方）
        System.out.println(2 << 2); // 8 2*2的2次方
        System.out.println(2 << 3); // 16
        System.out.println(2 << 4); // 32
        System.out.println(2 << 5); // 64

        System.out.println(3 << 2); // 12 3*2的2次方
        System.out.println(3 << 3); // 24 3*2的3次方

        System.out.println(1 << 3); // 8 1*2的3次方


        System.out.println("m的n次方====================");
        // Math.pow(m，n)，表示m的n次方
        System.out.println(Math.pow(2, 6));
        System.out.println(Math.pow(2.1, 6));

        System.out.println("指定进制====================");
        // 源的指定进制,转换为十进制结果
        System.out.println(Integer.valueOf("10", 2));
        System.out.println(Integer.valueOf("10", 8));
        System.out.println(Integer.valueOf("10", 10));
        System.out.println(Integer.valueOf("10", 16));
        System.out.println(Integer.valueOf("01", 16));
        System.out.println(Integer.valueOf("0a", 16));
    }

    @Test
    void test5() {
        /*
易混淆场景：该方法不用于普通字符串转换（如 "123"），而是查找系统属性。误用可能导致意外结果。

正确转换方式：若需将字符串转为整数，应使用 Integer.parseInt() 或 Integer.valueOf()。
         */

        // 获取系统属性 "thread.count"，不存在则返回 null
        Integer threads = Integer.getInteger("thread.count");
        System.out.println("threads = " + threads);

// 获取系统属性 "timeout"，不存在或无效则返回默认值 30
        Integer timeout = Integer.getInteger("timeout", 30);
        System.out.println("timeout = " + timeout);


    }

    /*
    1. Integer.valueOf(String str)
    功能：将字符串转换为 Integer 对象。
    返回值类型：返回的是一个 Integer 对象（包装类）。
    内部实现：实际上调用了 Integer.valueOf(Integer.parseInt(str))，即先通过 parseInt 将字符串转为基本类型 int，然后再将其包装为 Integer 对象。
    缓存机制：对于 -128 到 127 范围内的值，Integer.valueOf 会使用缓存的 Integer 对象，而不是每次都创建新的对象。
    异常：如果字符串不是有效的整数格式（如 null 或非数字字符），会抛出 NumberFormatException。

    2. Integer.parseInt(String str)
    功能：将字符串转换为基本类型 int。
    返回值类型：返回的是一个基本类型 int。
    内部实现：直接解析字符串并返回 int 值，没有包装类的额外操作。
    缓存机制：无缓存机制，因为返回的是基本类型 int。
    异常：如果字符串不是有效的整数格式（如 null 或非数字字符），会抛出 NumberFormatException。

    使用建议
    如果只需要基本类型 int，推荐使用 Integer.parseInt，因为它更高效。
    如果需要将整数存储到集合（如 List<Integer>）中，或者需要利用对象特性（如引用传递、方法调用等），则使用 Integer.valueOf。

     */
    @Test
    void test6() {
        String str1 = null;
// 定义一个字符串变量 str1，值为 "2"。

        try {
            Integer integer = Integer.valueOf(str1);
            System.out.println("integer = " + integer);
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException");
        }
        try {

            int i = Integer.parseInt(str1);
            System.out.println("i = " + i);
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException");
        }

        int i = NumberUtil.parseInt(str1, -1);
        System.out.println("i = " + i);

    }
}
