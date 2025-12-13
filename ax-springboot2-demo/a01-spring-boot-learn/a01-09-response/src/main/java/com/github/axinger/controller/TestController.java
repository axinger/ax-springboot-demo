package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestController.java
 * @description TODO
 * @createTime 2022年07月30日 18:26:00
 */

@RestController
public class TestController {

    @GetMapping("/test1")
    @ResponseBody
    public Object test1() {
        System.out.println("test1==========");
        return "123";
    }

    @GetMapping("/ip1")
    public Object test2() {
        System.out.println("test2==========");
        return List.of("1");
    }


    @GetMapping("/error1")
    public Object error1() {
        System.out.println("error1==========");
        int i = 1 / 0;
        return "123";
    }

    @GetMapping("/error2")
    public Object error2() throws Exception {
        System.out.println("error2==========");
        throw new Exception("error2");
    }
}
