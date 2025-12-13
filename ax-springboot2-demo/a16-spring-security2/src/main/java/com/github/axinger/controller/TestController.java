package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/work")
    public String test() {
        return "hello world";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
