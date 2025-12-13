package com.github.axinger.stream;

import com.github.axinger.Person;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AnyMatchTest {

    @Test
    void test1() {
        Optional<Person> p = Person.personList.stream().filter(x -> x.getAge() > 20).findFirst();
        System.out.println("p = " + p);
    }

    @Test
    void test2() {
        Optional<Person> p = Person.personList.stream().filter(x -> x.getAge() > 20).findAny();
        System.out.println("p = " + p);
    }

    @Test
    void test3() {
        boolean b = Person.personList.stream().allMatch(val -> val.getAge() > 20);
        System.out.println("b = " + b);
    }

    @Test
    void test4() {
        boolean b = Person.personList.stream().anyMatch(val -> val.getAge() > 20);
        System.out.println("b = " + b);
    }

    @Test
    void test5() {
        boolean b = Person.personList.stream().noneMatch(val -> val.getAge() > 20);
        System.out.println("b = " + b);
    }

    @Test
    void test6() {
        // 使用HashSet代替ArrayList以提高查找效率
        Set<String> set = new HashSet<>(List.of("安徽"));
        boolean b = Person.personList.stream().anyMatch(x -> set.contains(x.getAddress()));
        System.out.println("b = " + b);
    }


    @Test
    void test7() {
        // 使用HashSet代替ArrayList以提高查找效率
        Set<String> set = new HashSet<>(List.of("安徽"));
        List<Person> list = Person.personList.stream().filter(x -> set.contains(x.getAddress())).toList();
        System.out.println("list = " + list);
    }

    @Test
    void test8() {
        // 使用HashSet代替ArrayList以提高查找效率
        Set<String> set = new HashSet<>(List.of("安徽"));
        Optional<Person> list = Person.personList.stream().filter(x -> set.contains(x.getAddress())).findAny();
        System.out.println("list = " + list);
    }
}
