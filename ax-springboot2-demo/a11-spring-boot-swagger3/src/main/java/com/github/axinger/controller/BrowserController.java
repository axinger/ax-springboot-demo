package com.github.axinger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrowserController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/shutdown")
    public void shutdown() {
        System.exit(0);
    }
}
