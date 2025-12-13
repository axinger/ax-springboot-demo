package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方法引用构造器,类似方法,要有对应的构造函数
 */
public class LambdaFactoryTest {

    @Test
    void test1() {

        Supplier<Person> supplier = () -> Person.builder().build();

        System.out.println(supplier.get());

        Supplier<Person> supplier2 = Person::new;
        System.out.println("supplier2 = " + supplier2.get());

    }

    @Test
    void test2() {

        Function<String, Person> function1 = name -> Person.builder().name(name).build();
        Person person1 = function1.apply("jim");
        System.out.println("person1 = " + person1);

    }

}
