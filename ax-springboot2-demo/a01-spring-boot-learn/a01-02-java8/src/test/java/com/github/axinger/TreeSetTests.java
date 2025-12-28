package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class TreeSetTests {

    @Test
    void test1() {
        //TreeSet 排序集合
        Set<String> set = new TreeSet<>();
        set.add("2");
        set.add("1");
        set.add("10");
        set.add("10");
        System.out.println("set = " + set);
        TreeSet<Integer> set1 = set.stream().map(Integer::valueOf).collect(Collectors.toCollection(TreeSet::new));
        System.out.println("set1 = " + set1);
    }


    @Test
    void test2() {
        Set<Object> set = new HashSet<>();
        set.add("2");
        set.add("1");
        set.add("10");
        set.add("10");
        System.out.println("set = " + set);

        Set<Object> set1 = set.stream().sorted().collect(Collectors.toSet());
        System.out.println("set1 = " + set1);
    }

    @Test
    void test3() {
        /// 向下取整, [a, b)
        TreeSet<Integer> set = new TreeSet<>(Arrays.asList(10, 15, 20));

        /// 返回 小于或等于 e 的最大元素, 向下对齐,包含
        System.out.println(" set.floor(10) = " + set.floor(10));
        System.out.println(" set.floor(14) = " + set.floor(14));
        System.out.println(" set.floor(15) = " + set.floor(15));
        System.out.println(" set.floor(16) = " + set.floor(16));
        System.out.println(" set.floor(20) = " + set.floor(20));
        System.out.println("===================================================");
        /// 返回 大于或等于 e 的最小元素, 向上对齐,包含
        System.out.println(" set.ceiling(10) = " + set.ceiling(10));
        System.out.println(" set.ceiling(14) = " + set.ceiling(14));
        System.out.println(" set.ceiling(15) = " + set.ceiling(15));
        System.out.println(" set.ceiling(16) = " + set.ceiling(16));
        System.out.println(" set.ceiling(20) = " + set.ceiling(20));
        System.out.println("===================================================");
        /// 返回 小于 e 的最大元素	❌ 不包含,向下对齐,不包含
        System.out.println(" set.lower(10) = " + set.lower(10));
        System.out.println(" set.lower(14) = " + set.lower(14));
        System.out.println(" set.lower(15) = " + set.lower(15));
        System.out.println(" set.lower(16) = " + set.lower(16));
        System.out.println(" set.lower(20) = " + set.lower(20));

        System.out.println("===================================================");
        ///返回 大于 e 的最小元素	❌ 不包含,向上对齐,不包含
        System.out.println(" set.higher(10) = " + set.higher(10));
        System.out.println(" set.higher(14) = " + set.higher(14));
        System.out.println(" set.higher(15) = " + set.higher(15));
        System.out.println(" set.higher(16) = " + set.higher(16));
        System.out.println(" set.higher(20) = " + set.higher(20));
    }
}
