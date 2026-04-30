package com.github.axinger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * 登录控制器
 */
@Controller
public class LoginController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 授权确认页面
     */
    @GetMapping("/oauth2/consent")
    public String consent() {
        return "consent";
    }
}
