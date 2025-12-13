package com.github.axinger.stream;

import com.github.axinger.Person;

import java.util.Comparator;
import java.util.Optional;

public class StreamTest_max_str {
    public static void main(String[] args) {

        final Optional<Person> optionalPerson = Person.personList.stream().max(Comparator.comparingInt(Person::getAge));
        System.out.println("员工年龄最大值员工：" + optionalPerson.get());

        final Optional<String> optional = Person.personList.stream().max(Comparator.comparingInt(Person::getAge)).map(Person::getName);
        System.out.println("员工年龄最大值的姓名 = " + optional.orElse(null));
    }
}
