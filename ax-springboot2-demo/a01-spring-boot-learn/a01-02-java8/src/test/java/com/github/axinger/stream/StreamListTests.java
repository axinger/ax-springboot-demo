package com.github.axinger.stream;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class StreamListTests {

    @Test
    void test1() {

        List<String> list = Arrays.asList("a", "b", "c");
//        List<String> list = null;


        Stream.ofNullable(list).forEach(val -> System.out.println("val = " + val));


        if (list.size() > 2) {
            list = null;
        }

        for (String s : Optional.ofNullable(list).orElse(new ArrayList<>())) {

            System.out.println("s = " + s);
        }
    }
}
