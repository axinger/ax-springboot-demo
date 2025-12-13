package com.github.axinger.event;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MyCustomEventListener {

    // @EventListener(classes = MyCustomEvent.class)
    @EventListener
    public void myCustomEvent(MyCustomEvent event) { // 参数为指定格式
        System.out.println("接收到MyCustomEvent - " + event.getSource());
    }

    @EventListener(classes = {MyCustomEvent.class, MyCustomEvent2.class})
    // @EventListener
    public void myCustomEvent2(ApplicationEvent event) { // 参数为通用格式
        System.out.println("接收到MyCustomEvent2 - " + event.getSource());
        System.out.println("接收到MyCustomEvent2 - " + event.getTimestamp());
    }

    @EventListener()
    public void testEvent2(ApplicationStartedEvent event) {
        System.out.println("ApplicationStartedEvent = " + event.toString());
    }
}
