package com.github.axinger.model;

import lombok.Data;

@Data
public class AClass {

    public void test() {
        System.out.println("this = " + this);
    }
}
