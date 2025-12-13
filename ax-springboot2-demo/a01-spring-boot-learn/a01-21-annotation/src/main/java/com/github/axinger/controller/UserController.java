package com.github.axinger.controller;

import com.github.axinger.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/user")
public class UserController {

    @NotEmpty(message = "name不能为空")
//    private String name;
    public String name;
    @Autowired
    private MyService myService;

    @GetMapping("/list")
    public void list() {

    }

    @GetMapping("/test")
    public String test() {
        return myService.testMethod("1111");
    }
}
