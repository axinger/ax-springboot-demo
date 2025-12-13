package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {


    @GetMapping("/")
    public Object index() {
        Map<String, Object> result = new HashMap<>();
        result.put("1", "首页");
        return result;
    }

    @GetMapping("/home")
    public Object home() {
        return "Hello World";
    }
}
