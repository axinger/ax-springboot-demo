package com.github.axinger.controller;

import com.github.axinger.dao.PersonJpaRepository;
import com.github.axinger.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @GetMapping("/data")
    public Object data() {
        return List.of(1);
    }

    @GetMapping("/list")
    public Object findAll() {
        List<Person> all = personJpaRepository.findAll();
        return all;
    }
}
