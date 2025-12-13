package com.github.axinger.controller;

import com.github.axinger.entity.User;
import com.github.axinger.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName UserController.java
 * @description TODO
 * @createTime 2022年06月22日 15:55:00
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public Object list() {
        return userService.list();
    }


    @RequestMapping("/test1")
    public Object test1() {
        User user = new User();
        user.setName("tom");
        userService.save(user);
        return true;
    }

    @RequestMapping("/test2")
    public Object test2() {
        return userService.saveAndDel();
    }

}
