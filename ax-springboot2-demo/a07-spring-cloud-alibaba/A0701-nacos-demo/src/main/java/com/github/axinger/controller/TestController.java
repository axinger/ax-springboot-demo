package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/order/1")
    public Object order1() {
        return "/order/1";
    }

    @GetMapping("/order/2")
    public Object order2() {
        return "/order/2";
    }

    @GetMapping("/order/3")
    public Object order3() {
        return "/order/3";
    }


    @GetMapping("/payment/1")
    public Object payment1() {
        return "/payment1/1";
    }
}
