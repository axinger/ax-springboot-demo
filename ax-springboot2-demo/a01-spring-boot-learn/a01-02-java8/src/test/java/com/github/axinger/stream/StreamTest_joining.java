package com.github.axinger.stream;

import com.github.axinger.Person;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest_joining {

    @Test
    public void test1() {

        String names = Person.personList.stream().map(Person::getName).collect(Collectors.joining(","));
        System.out.println("所有员工的姓名：" + names);


        String string = Person.personList.stream().map(Person::getName).collect(Collectors.joining("-"));
        System.out.println("拼接后的字符串：" + string);


        List<String> list = Arrays.asList("A", "B", "C");
        String string2 = list.stream().collect(Collectors.joining("-", "头", "尾"));
        System.out.println("拼接后的字符串：" + string2);
    }
}
