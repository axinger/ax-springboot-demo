package com.github.axinger.event;

import lombok.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventListener implements ApplicationListener<CustomEvent> {


    @Override
    public void onApplicationEvent(@NonNull CustomEvent event) {
        System.out.println("收到消息11 = " + event);
    }

    @EventListener(CustomEvent.class)
    public void test1(CustomEvent event) {
        System.out.println("收到消息22 = " + event);
    }

    @EventListener
    public void test3(CustomEvent event) {
        System.out.println("收到消息33 = " + event);
    }

}
