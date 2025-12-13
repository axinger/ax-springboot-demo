package com.github.axinger.xml.model;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Tests_2 {

    @Test
    void test_person_auto() {

        ApplicationContext context = new ClassPathXmlApplicationContext("bean5.xml");
        Person person = context.getBean("person", Person.class);
        System.out.println("person = " + person);


    }
}
