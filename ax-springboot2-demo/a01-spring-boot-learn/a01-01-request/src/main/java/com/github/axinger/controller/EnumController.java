package com.github.axinger.controller;

import com.github.axinger.bean.Person;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class EnumController {

    @PostMapping("/gender")
    public Object login(@RequestBody Person person) {
        System.out.println("枚举作为参数 = " + person);
        return Map.of("枚举参数", person);
    }

}
