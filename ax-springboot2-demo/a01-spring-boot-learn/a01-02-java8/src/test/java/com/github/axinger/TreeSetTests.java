package com.github.axinger;

import org.junit.jupiter.api.Test;

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
}
