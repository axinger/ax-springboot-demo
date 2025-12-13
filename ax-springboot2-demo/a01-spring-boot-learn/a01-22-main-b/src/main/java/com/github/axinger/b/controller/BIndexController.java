package com.github.axinger.b.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class BIndexController {
    @Value("${axing.person.id}")
    private String name;

    @GetMapping("/")
    public Object login() {
        return List.of(name);
    }


}
