package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/filter")
public class TestFilterController {


    @GetMapping("/1")
    public Object test1() {
        return Map.of("data", "test1");
    }

    // 测试filter,在Filter中报错
    @GetMapping("/2")
    public Object test2() {
        return Map.of("data", "test2");
    }
}
