package com.github.axinger.controller;

import com.github.axinger.domain.Person;
import com.github.axinger.service.PersonService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private PersonService personService;
    @Autowired
    private TestService1 testService1;
    @Autowired
    private TestService2 testService2;

    @GetMapping
    public void test() {
        Person person = new Person();
        person.setName("小明");
        boolean save = personService.save(person);


        List<Person> list = personService.list();
        System.out.println(list);

    }

    @SneakyThrows
    @GetMapping("/1")
    public void test1() {
        testService1.test1();

    }

    @SneakyThrows
    @GetMapping("/2")
    public void test2() {
        testService1.test2();

    }

    @GetMapping("/4")
    public void test4() {
        testService1.test4();

    }

    @GetMapping("/21")
    public void test21() {
        testService2.test();

    }
}
