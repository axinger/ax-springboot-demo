package com.github.axinger.aop.model;

import org.springframework.stereotype.Component;

@Component
public class AopUser {
    public void add() {
        System.out.println("add...........");
    }
}
