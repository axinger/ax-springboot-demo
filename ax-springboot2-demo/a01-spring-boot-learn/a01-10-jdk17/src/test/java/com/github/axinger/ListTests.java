package com.github.axinger;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ListTests {

    @Test
    void test() {

        //  List.of 不可变,会报错
        List<Integer> list = List.of(1);
        System.out.println("list = " + list);
        // list.add(2);

        // guava,可变
        ArrayList<Integer> list1 = Lists.newArrayList(1);
        list1.add(2);
        System.out.println("list1 = " + list1);
    }
}
