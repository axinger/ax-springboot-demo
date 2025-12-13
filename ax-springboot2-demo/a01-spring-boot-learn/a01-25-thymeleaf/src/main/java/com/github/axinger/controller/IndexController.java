package com.github.axinger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

//    let id = [[${id}]]
//    前端取值语法

    @RequestMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @RequestMapping("/test")
    public String login() {
        return "index";
    }
}
