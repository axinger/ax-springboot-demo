package com.github.axinger.stream;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class StreamFlatMapTests {
    // "Hello","World" 拆开每个字符集
    @Test
    void test1() {
        List<String> list = Arrays.asList("Hello", "World");
        List<String> listNew = list.stream().flatMap(s -> {
            // 将每个元素转换成一个stream
            String[] split = s.split("");
            return Arrays.stream(split);
        }).toList();

        System.out.println("处理前的集合：" + list);
        System.out.println("处理后的集合：" + listNew);
    }

    // List<List<String>> 拍扁
    @Test
    void test2() {
        List<List<String>> list = Arrays.asList(Arrays.asList("小明", "小红"), Arrays.asList("升高", "体重"));
        // 将每个元素转换成一个stream
        List<String> list1 = list.stream().flatMap(Collection::stream).toList();
        System.out.println("list1 = " + list1); // list1 = [小明, 小红, 升高, 体重]
    }

    //  Map<String,List<String>> 拍扁
    @Test
    void test3() {
        Map<String, List<String>> map = new HashMap<>() {{
            put("姓名", Arrays.asList("小明", "小红", "小明"));
            put("属性", Arrays.asList("升高", "体重"));
        }};
        // 将每个元素转换成一个stream
        List<String> list1 = map.entrySet().stream().flatMap(val -> val.getValue().stream()).toList();
        System.out.println("list1 = " + list1); // list1 = [小明, 小红, 升高, 体重]

        Set<String> set = map.entrySet().stream().flatMap(val -> val.getValue().stream()).collect(Collectors.toSet());
        System.out.println("set = " + set);
    }
}
