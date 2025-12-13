package com.github.axinger.stream;

import com.github.axinger.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StreamFindTest {

    static List<Person> personList;

    @BeforeAll
    static void before() {
        personList = new ArrayList<>() {{
            add(Person.builder()
                    .id(1)
                    .name("jim")
                    .sex("男")
                    .age(10)
                    .address("安徽")
                    .build());
            add(Person.builder()
                    .id(1)
                    .name("lili")
                    .sex("女")
                    .age(10)
                    .address("安徽")
                    .build());
            add(Person.builder()
                    .id(1)
                    .name("Lucy")
                    .sex("女")
                    .age(18)
                    .address("安徽")
                    .build());
            add(Person.builder()
                    .id(1)
                    .name("小红")
                    .sex("女")
                    .age(20)
                    .address("江苏")
                    .build());
        }};
    }


    @Test
    void test1() {

        personList.stream().filter(val -> val.getName().equals("jim")).findFirst().ifPresent(val -> {

            System.out.println("val = " + val);

        });
    }
}
