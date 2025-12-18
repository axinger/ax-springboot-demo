package com.github.axinger.controller;

import com.github.axinger.domain.A35UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HtmlController {

    @GetMapping("/")
    public String home(HttpSession session) {
        A35UserEntity user = (A35UserEntity) session.getAttribute("user");
        if (user != null) {
            // 用户已登录，重定向到任务列表页面
            return "redirect:/task";
        }
        // 用户未登录，重定向到登录页面
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // 这会渲染 templates/login.html 模板
    }

    @GetMapping("/task")
    public String task() {
        return "task";
    }


}
