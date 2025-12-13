package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

class DemoAppTest {


    @Test
    void test_java11() {

        /// List不可变集合,只读集合 新api
        List list1 = List.of(1, 2);
//        list1.add(3); // 报错
        System.out.println("list1 = " + list1);

        Map map = Map.of("name", "jim", "age", 12);
        System.out.println("map = " + map);
    }

    @Test
    void test_java11_stream() {

        List<Integer> list1 = List.of(45, 43, 76, 4, 5);

        // takewhile() 方法接受所有值，直到谓词返回false为止,才停止。
        // 也就是从第1个元素一直往后判断，符合条件的就进行处理，如果中间遇见一个不符合的，整个流程结束。
        list1.stream()
                .takeWhile(val -> val < 50)
                .forEach(System.out::println); // 45,43  ,

        // dropWhile()方法丢弃所有值，直到它与谓词匹配为止。行为与 takeWhile 相反，返回剩余的元素。
        // 也就是从第1个元素一直往后判断，如果符合条件的就继续往下判断，当发现元素不符合条件，那么处理后面的那些元素。

        list1.stream()
                .dropWhile(val -> val < 50)
                .forEach(val -> System.out.println("dropWhile val = " + val)); // 76,4,5

        // 终止,以前用  .limit(10),感觉 .limit(10)好用
        Stream.iterate(1, i -> i < 5, i -> i + 1)
                .forEach(System.out::println);


        // Optional 和 Stream 之间的结合也得到了改进。 现在可以通过 Optional 的新方法 stream() 将一个 Optional 对象转换为一个 (可能是空的) Stream 对象


        /** 以前写法  .orElse(new ArrayList<>())
         *  List listNull = null;
         *         Optional.ofNullable(listNull)
         *                 .orElse(new ArrayList<>())
         *                 .stream()
         *                 .forEach(val-> System.out.println("Optional 和 Stream val = " + val));
         */
        List listNull = null;
        Optional.ofNullable(listNull)
                .stream()
                .forEach(val -> System.out.println("Optional 和 Stream val = " + val));
    }

    // Java 10
    @Test
    void test_java11_var() {

        // 因为变量声明有时候太长，会导致可读性变差，所以通过类型名 var 来统一声明一个局部变量。适用的范围只能在局部代码块中，它会自动推断类型
        /**
         * 它不适用于以下几种情况：
         *
         *     情况1：没有初始化的局部变量声明
         *     情况2：方法的返回类型
         *     情况3：方法的参数类型
         *     情况4：构造器的参数类型
         *     情况5：属性
         *     情况6：catch块
         *
         * 个人觉得类型推断的这一新特性，并没有带来什么可读性的好处。
         */
        var list = new ArrayList<>();
//        var list1 = null;
        list.add(1);
        list.forEach(val -> System.out.println("var val = " + val));

    }

    // Java 11

    @Test
    void test_jdk11() {

        // 是否为空白
        boolean isBlank = " ".isBlank();
        System.out.println("isBlank = " + isBlank);
        System.out.println("\" \"length = " + " ".length());


        // "" isEmpty  true
        // " " isEmpty false
        System.out.println("\" \"isEmpty = " + " ".isEmpty());
        System.out.println("\"\"isEmpty = " + "".isEmpty());

        // 复制
        System.out.println("repeat = " + "Ab".repeat(3));

    }
}
