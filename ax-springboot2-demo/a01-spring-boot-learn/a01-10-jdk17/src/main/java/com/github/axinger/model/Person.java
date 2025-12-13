package com.github.axinger.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    private Dog dog;

}
