package com.github.axinger;

import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class EventBusTests {

    @Test
    public void test() throws InterruptedException {


        // 创建事件总线实例
        EventBus eventBus = new EventBus();

// 创建并注册处理器
        EventHandler handler = new EventHandler();
        eventBus.register(handler);

// 发布事件
        eventBus.post(new MyEvent("aaaa"));

        TimeUnit.SECONDS.sleep(4);
    }
}
