package com.github.axinger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * 首页控制器
 */
@Controller
public class HomeController {

    /**
     * 首页
     */
    @GetMapping({"/", "/index"})
    public String index(Model model, Principal principal) {
        model.addAttribute("loggedIn", principal != null);
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }
}
