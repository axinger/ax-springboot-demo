package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {


    @GetMapping("/my1")
    public Object count1() {
        return "订单";
    }

    @GetMapping("/my2/my3")
    public Object count12() {
        return "订单2";
    }
}
