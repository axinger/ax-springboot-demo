package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 方法引用排序
 */
public class ComparatorTest {

    @Test
    void test1() {

        List<Person> persons = new ArrayList<Person>() {{
            add(Person.builder()
                    .id(1)
                    .build());
            add(Person.builder()
                    .id(1)
                    .build());
            add(Person.builder()
                    .id(1)
                    .build());
            add(Person.builder()
                    .id(1)
                    .build());

        }};

        List<Person> result = persons.stream()
                .sorted(Comparator.comparing(Person::getName))
                .collect(Collectors.toList());

        System.out.println("result = " + result);

        List<Person> result2 = persons.stream()
                .sorted(
                        Comparator.comparing(Person::getName)
                                .thenComparing(Person::getAge)
                )
                .collect(Collectors.toList());

        System.out.println("result2 = " + result2);


    }


}
