package com.github.axinger.controller;

import com.github.axinger.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // 用户已登录，重定向到任务列表页面
            return "redirect:/tasks";
        }
        return "redirect:/auth/login";
    }
}