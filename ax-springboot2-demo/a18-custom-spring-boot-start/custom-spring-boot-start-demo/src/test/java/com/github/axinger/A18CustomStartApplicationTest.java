package com.github.axinger;


import com.axing.starter.service.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class A18CustomStartApplicationTest {

    @Autowired
    private HelloService helloService;

    @Test
    void test1() {
        Map<String, Object> map = helloService.sayHello("jim");
        System.out.println("map = " + map);
    }
}
