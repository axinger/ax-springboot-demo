package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/test1")
    public Object index() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", "test1");
        return result;
    }

    @GetMapping("/2")
    public Object home() {
        Map<String, Object> result = new HashMap<>();
        result.put("data", "test2");
        return result;
    }
}
