package com.github.axinger;

import lombok.Data;

@Data
public abstract class Animal {

    String name;

    abstract void eat();

    void eat1() {

    }

}
