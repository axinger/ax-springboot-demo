package com.github.axinger.stream;

import com.github.axinger.Person;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SteamIterableTests {

    @Test
    void test1() {
        Iterable<String> iterable
                = Arrays.asList("Testing", "Iterable", "conversion", "to", "Stream");
        List<String> result = StreamSupport.stream(iterable.spliterator(), false)
                .map(String::toUpperCase)
                .toList();
        System.out.println("result = " + result);

        List<String> result2 = StreamSupport.stream(iterable.spliterator(), false)
                .map(String::toLowerCase)
                .toList();
        System.out.println("result2 = " + result2);
    }


    @Test
    void test2() {
        List<Person> persons = new ArrayList<>();

        persons.add(Person.builder().id(1).name("jim").sex("男").build());
        persons.add(Person.builder().id(2).name("jim").sex("女").build());
        persons.add(Person.builder().id(4).name("jim").sex("男").build());
        persons.add(Person.builder().id(3).name("jim").sex("女").build());
        persons.add(Person.builder().id(6).name("jim").sex("男").build());
        persons.add(Person.builder().id(4).name("jim").sex("女").build());


        List<Person> list = persons.stream().sorted(Comparator.comparing(Person::getId)).toList();
        System.out.println("list = " + list);

        Map<String, List<Person>> map1 = list.stream().collect(Collectors.groupingBy(Person::getSex));
        System.out.println("map1 = " + map1);


    }
}
