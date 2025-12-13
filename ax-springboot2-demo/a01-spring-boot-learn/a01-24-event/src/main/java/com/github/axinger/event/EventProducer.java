package com.github.axinger.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class EventProducer<E extends ApplicationEvent> {

    // 使用CopyOnWriteArrayList以确保线程安全
    private final List<ApplicationListener<E>> customEventListeners = new CopyOnWriteArrayList<>();


    public void produceEvent(E o) {
        // 通知所有注册的监听器，添加异常处理以确保一个监听器的失败不会影响其他监听器
        for (ApplicationListener<E> listener : customEventListeners) {
            try {
                listener.onApplicationEvent(o);
            } catch (Exception e) {
                log.error("Error occurred while notifying event listener", e);

            }
        }
    }

    public void addCustomEventListener(ApplicationListener<E> listener) {
        // 添加监听器时的线程安全操作
        synchronized (customEventListeners) {
            customEventListeners.add(listener);
        }
    }

    public void removeCustomEventListener(ApplicationListener<E> listener) {
        // 删除监听器时的线程安全操作
        synchronized (customEventListeners) {
            customEventListeners.remove(listener);
        }
    }
}
