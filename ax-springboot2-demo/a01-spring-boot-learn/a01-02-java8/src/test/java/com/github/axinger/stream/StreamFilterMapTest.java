package com.github.axinger.stream;

import com.github.axinger.Person;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreamFilterMapTest {


    @Test
    void test1() {
        List<String> list = Person.personList.stream().filter(x -> x.getAge() > 20)
                .map(Person::getName)
                .toList();
        System.out.println("age>20的 姓名：" + list);
    }


    @Test
    void test2() {

        Person.personList.stream().filter(x -> x.getAge() > 20).forEach(System.out::println);
    }

    @Test
    void test3() {
        // 使用HashSet代替ArrayList以提高查找效率
        Set<String> set = new HashSet<>(List.of("安徽"));
        List<Person> list1 = Person.personList.stream().filter(x -> set.contains(x.getAddress())).toList();
        System.out.println("list1 = " + list1);
    }

    @Test
    void test4() {
        // 使用HashSet代替ArrayList以提高查找效率
        Set<String> set = new HashSet<>(List.of("安徽"));
        boolean b = Person.personList.stream().anyMatch(x -> set.contains(x.getAddress()));
        System.out.println("b = " + b);
    }
}
