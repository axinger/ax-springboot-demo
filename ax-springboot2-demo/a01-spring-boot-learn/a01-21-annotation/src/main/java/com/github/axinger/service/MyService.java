package com.github.axinger.service;

import com.github.axinger.annotation.MyCustomAnnotation;
import org.springframework.stereotype.Service;


@Service
public class MyService {

    //    @MyCustomAnnotation(value = "TestMethod")
    @MyCustomAnnotation(value = "${axing.person}", num = "${axing.num:11}")
    public String testMethod(String param) {
        System.out.println("Executing testMethod with param: " + param);
        return "Hello, " + param;
    }
}
