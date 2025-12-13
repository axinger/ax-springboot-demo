package com.github.axinger;

// import com.google.common.collect.Tuple;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TupleTest {

    // 没有
    @Test
    void test() {
//        Tuple4<Integer, String, Double, Boolean> tuple = Tuple.of(1, "Hello", 3.14, true);

        Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>("age", 30);
        entry.setValue(30);
        String key = entry.getKey();
        System.out.println("key = " + key);

        Pair<String, Integer> pair = Pair.of("age", 30);

        System.out.println("pair.getLeft() = " + pair.getLeft());

        System.out.println("pair.getRight() = " + pair.getRight());

        List<Object> list = Arrays.asList("Alice", 25, 170.5);
        Object name = list.get(0); // 需要强制类型转换
        System.out.println("name = " + name);
        Integer age = (Integer) list.get(1);
        System.out.println("age = " + age);
    }
}
