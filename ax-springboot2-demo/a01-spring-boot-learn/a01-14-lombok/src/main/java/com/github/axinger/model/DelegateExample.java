package com.github.axinger.model;


import lombok.experimental.Delegate;

public class DelegateExample {

    @Delegate
    private final AClass aClass = new AClass();

}
