package com.github.axinger.controller;

import com.github.axinger.domain.PersonEntity;
import com.github.axinger.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HomeController {

    @Autowired
    private PersonService personService;

    @GetMapping("/data")
    public Object data() {
        return List.of(1);
    }

    @GetMapping("/list")
    public Object test() {
        List<PersonEntity> list = personService.list();
        return list;
    }

}
