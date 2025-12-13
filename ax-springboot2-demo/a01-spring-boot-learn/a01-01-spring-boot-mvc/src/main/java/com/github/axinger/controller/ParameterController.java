package com.github.axinger.controller;

import com.github.axinger.config.SpiltList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ParameterController {

    @GetMapping("/p1")
    public Object stringToList(@RequestParam @SpiltList List<String> name) {
        return name;
    }
}
