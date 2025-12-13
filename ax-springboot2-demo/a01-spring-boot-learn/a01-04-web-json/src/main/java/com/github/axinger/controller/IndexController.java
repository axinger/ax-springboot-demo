package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class IndexController {

    @GetMapping("/")
    public ModelAndView index() {
        RedirectView redirectView = new RedirectView("/doc.html");

        return new ModelAndView(redirectView);
    }
}
