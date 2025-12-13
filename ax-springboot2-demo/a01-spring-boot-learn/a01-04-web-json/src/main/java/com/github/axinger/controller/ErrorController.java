package com.github.axinger.controller;

import com.github.axinger.entity.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ErrorController {

    @GetMapping(value = "/zero")
    public Object zero() {
        int i = 1 / 0;
        return new Object();
    }

    @GetMapping(value = "/objNull")
    public Object json() {
        Person person = null;
        person.getDog().getString();
        return new Object();
    }

}
