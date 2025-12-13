package com.github.axinger.event;

import org.springframework.context.ApplicationEvent;


public class MyCustomEvent2 extends ApplicationEvent {

    public MyCustomEvent2(Object source) {
        super(source);
    }

}
