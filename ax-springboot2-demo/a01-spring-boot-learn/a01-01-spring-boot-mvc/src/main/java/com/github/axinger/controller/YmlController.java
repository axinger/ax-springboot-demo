package com.github.axinger.controller;

import com.github.axinger.model.bean.HumitureRuleProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/a")
public class YmlController {


    @Autowired
    private HumitureRuleProperties humitureRuleProperties;

    @GetMapping("/humidity")
    public Object test2() {
        return humitureRuleProperties;
    }
}
