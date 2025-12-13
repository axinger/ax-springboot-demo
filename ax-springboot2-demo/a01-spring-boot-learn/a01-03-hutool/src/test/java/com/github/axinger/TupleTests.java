package com.github.axinger;

import cn.hutool.core.lang.Tuple;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TupleTests {

    @Test
    void test1() {
        // 内部就是数组,强转类型
        Tuple tuple = new Tuple(1, 1L, "1");
        System.out.println("tuple = " + tuple);
        Integer integer = tuple.<Integer>get(0);
        System.out.println("integer = " + integer);


        List<Object> list = List.of(1, "1", 1L);
        System.out.println("list = " + list);
        Object o = list.get(0);
        System.out.println("o.getClass() = " + o.getClass());


        ArrayList<Object> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(1L);
        System.out.println("list2 = " + list2);
    }
}
