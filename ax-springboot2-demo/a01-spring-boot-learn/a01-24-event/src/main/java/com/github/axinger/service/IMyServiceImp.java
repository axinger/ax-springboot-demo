package com.github.axinger.service;

import com.github.axinger.event.CustomEvent;
import com.github.axinger.event.EventProducer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IMyServiceImp implements IMyService {
    private final CustomEventListener listener = new CustomEventListener();
    // 使用AtomicInteger保证线程安全
    private final AtomicInteger count = new AtomicInteger();
    private volatile boolean isListening = false;

    @Autowired
    private EventProducer<CustomEvent> eventProducer;

    @SneakyThrows
    @Override
    @Async
    public void test(String name) {

        count.set(0);
        try {
            startListening(listener);

            while (count.get() < 3) {
                System.out.println("name = " + name + "count = " + count.get());
                TimeUnit.SECONDS.sleep(2);
            }
        } finally {
            // 确保在方法结束时调用stopListening，避免资源泄露
            stopListening(listener);
        }
    }

    private void startListening(ApplicationListener<CustomEvent> listener) {
        if (!isListening) {
            eventProducer.addCustomEventListener(listener);
            isListening = true;
            System.out.println("Listening for events...");
        }
    }

    private void stopListening(ApplicationListener<CustomEvent> listener) {
        if (isListening) {
            eventProducer.removeCustomEventListener(listener);
            isListening = false;
            System.out.println("Stopped listening for events.");
        }
    }

    @SneakyThrows
    public void test2(String name) {

        AtomicInteger count = new AtomicInteger();
        boolean isListening = false;

        // 创建一个ApplicationListener的实现类
        ApplicationListener<CustomEvent> listener = event -> {
            System.out.println(name + "事件event = " + event.getSource());
            // 在这里处理事件
            count.set((Integer) event.getSource());
        };


        count.set(0);
        try {
            startListening(listener);

            while (count.get() < 3) {
                System.out.println("name = " + name + ",count = " + count.get());
                TimeUnit.SECONDS.sleep(2);
            }
        } finally {
            // 确保在方法结束时调用stopListening，避免资源泄露
            stopListening(listener);
        }
    }

    // 内部类，用于处理自定义事件
    private class CustomEventListener implements ApplicationListener<CustomEvent> {
        @Override
        public void onApplicationEvent(CustomEvent event) {
            // 使用getAndIncrement保证线程安全的递增
            count.set((Integer) event.getSource());
        }
    }
}
