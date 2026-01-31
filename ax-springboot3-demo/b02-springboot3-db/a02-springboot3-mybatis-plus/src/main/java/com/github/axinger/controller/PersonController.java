package com.github.axinger.controller;

import com.github.axinger.config.MyTenantLineHandler;
import com.github.axinger.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/list")
    public Object data() {
        MyTenantLineHandler.setTenantId("100");
        return personService.list();
    }


}
