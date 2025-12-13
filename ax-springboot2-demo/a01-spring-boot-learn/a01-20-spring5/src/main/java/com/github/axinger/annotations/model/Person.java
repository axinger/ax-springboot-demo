package com.github.axinger.annotations.model;

import org.springframework.stereotype.Repository;

@Repository
public class Person {

    private String name;

    public void add() {
        System.out.println("add........Person");
    }
}
