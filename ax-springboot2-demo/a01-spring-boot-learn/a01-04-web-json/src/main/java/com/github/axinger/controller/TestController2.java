package com.github.axinger.controller;

import com.github.axinger.desensitization.dto.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/desensitization")
public class TestController2 {

    @GetMapping("/test")
    public User getUser() {
        User user = new User();
        user.setUsername("张无忌");
        user.setIdcard("214578859632145123");
        user.setPhone("18545678956");
        user.setAddress("武当山");
        return user;
    }
}
