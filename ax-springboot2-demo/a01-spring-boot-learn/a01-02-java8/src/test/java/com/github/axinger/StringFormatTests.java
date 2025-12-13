package com.github.axinger;

import org.junit.jupiter.api.Test;

public class StringFormatTests {
    @Test
    public void test1() {
        int number = 1234567890;

        // 使用普通的%d格式化
        System.out.println("使用%d格式化: " + String.format("%d", number));

        // 使用%,d格式化，添加千位分隔符
        System.out.println("使用%,d格式化: " + String.format("%,d", number));

        // 更多示例
        System.out.println("\n更多示例:");
        System.out.println("%,d with 1000: " + String.format("%,d", 1000));
        System.out.println("%d with 1000: " + String.format("%d", 1000));
        System.out.println("%,d with 1234567: " + String.format("%,d", 1234567));
        System.out.println("%d with 1234567: " + String.format("%d", 1234567));
    }

    @Test
    public void test2() {
        System.out.println("右对齐=" + String.format("%5s", "A"));
        System.out.println("右对齐=" + String.format("%-5s", "A") + "<<<");
    }

    @Test
    public void test3() {
        // 浮点数格式化
        double d = 123.456789;
        System.out.println("默认浮点数: " + String.format("%f", d));
        System.out.println("保留2位小数: " + String.format("%.2f", d));
        System.out.println("保留4位小数: " + String.format("%.4f", d));
        System.out.println("总宽度为10，保留2位小数: " + String.format("%10.2f", d));
        System.out.println("左对齐，总宽度为10，保留2位小数: " + String.format("%-10.2f", d) + "<<<");

        // 科学计数法
        System.out.println("科学计数法: " + String.format("%e", d));
    }

    @Test
    public void test4() {
        // 整数格式化
        int i = 42;
        System.out.println("十进制: " + String.format("%d", i));
        System.out.println("八进制: " + String.format("%o", i));
        System.out.println("十六进制(小写): " + String.format("%x", i));
        System.out.println("十六进制(大写): " + String.format("%X", i));

        // 前导零填充
        System.out.println("2位数字，前导零填充: " + String.format("%02d", i));
        System.out.println("5位数字，前导零填充: " + String.format("%05d", i));
    }

    @Test
    public void test5() {
        // 日期时间格式化
        java.util.Date date = new java.util.Date();
        System.out.println("完整日期时间: " + String.format("%tF %tT", date, date));
        System.out.println("日期(ISO标准): " + String.format("%tF", date));
        System.out.println("时间(HH:mm:ss): " + String.format("%tT", date));
        System.out.println("年份: " + String.format("%tY", date));
        System.out.println("月份: " + String.format("%tm", date));
        System.out.println("日期: " + String.format("%td", date));
    }

    @Test
    public void test6() {
        // 百分比格式化
        double percent = 0.1234;
        System.out.println("百分比: " + String.format("%,.2f%%", percent * 100));

        // 复杂示例
        String name = "张三";
        int age = 25;
        double score = 98.5;
        String format = String.format("姓名: %s, 年龄: %d, 成绩: %.1f分", name, age, score);
        System.out.println(format);
    }
}
