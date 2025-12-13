package com.github.axinger.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping("/demo1")
    public Object demo1() {
        return Map.of("time", LocalDateTime.now());
    }

    @PostMapping("/demo2")
    public Object demo2(@RequestBody Map<String, Object> map) {
        System.out.println("map = " + map);
        return Map.of("time", LocalDateTime.now());
    }


}
