package com.github.axinger;

import com.google.common.eventbus.Subscribe;

public class EventHandler {
    @Subscribe
    public void handleMyEvent(MyEvent event) {
        // 处理 MyEvent 事件的逻辑
        System.out.println("event = " + event.getEvent());
    }
}
