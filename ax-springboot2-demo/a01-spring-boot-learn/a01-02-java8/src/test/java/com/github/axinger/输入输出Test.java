package com.github.axinger;

import java.io.Console;
import java.util.Scanner;


class SystemInExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // 将 System.in 包装为 Scanner

        System.out.print("请输入您的姓名: ");
        String name = scanner.nextLine(); // 读取一行字符串
        System.out.println("您好，" + name);

        System.out.print("请输入您的年龄: ");
        int age = scanner.nextInt(); // 读取整数
        System.out.println("您的年龄是: " + age);
    }
}

class ConsoleExample {
    public static void main(String[] args) {
        // 获取 Console 对象
        Console console = System.console();
        if (console == null) {
            System.out.println("无法获取控制台对象，可能在 IDE 中运行。");
            return;
        }

        // 输出提示信息并读取用户输入
        String name = console.readLine("请输入您的姓名: ");
        console.printf("您好，%s！\n", name);

        // 隐藏输入（如密码）
        char[] password = console.readPassword("请输入密码: ");
        console.printf("您输入的密码长度是：%d\n", password.length);
    }
}
