package com.github.axinger.controller;

import com.github.axinger.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ConsumeController.java
 * @description TODO
 * @createTime 2022年06月24日 15:47:00
 */
@RestController
public class ConsumeController {

    @Autowired
    private ConsumeService consumeService;

    @GetMapping("/")
    public Object index() {
        return "AAAA";
    }

    @GetMapping("/test1")
    public Object test1() {
        final Object test = consumeService.test();
        System.out.println("test = " + test);
        return test;
    }
}
