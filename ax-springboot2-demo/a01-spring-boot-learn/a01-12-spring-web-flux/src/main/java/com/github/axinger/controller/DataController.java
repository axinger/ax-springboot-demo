package com.github.axinger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class DataController {

    private final CopyOnWriteArrayList<ServerSentEvent<String>> eventCache = new CopyOnWriteArrayList<>();
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @EventListener
    public void onDataChangeEvent(DataChangeEvent event) {
        // 当数据变化事件触发时，将数据推送到事件缓存中
        if (event.getSource() instanceof Map<?, ?> map) {

        }
        eventCache.add(ServerSentEvent.builder(event.getSource().toString()).build());

    }

    @GetMapping("/data")
    public Flux<ServerSentEvent<String>> getData() {
        // 创建一个自定义Flux，用于监听数据变化事件
        return Flux.create(emitter -> {
            // 将事件缓存中的数据推送到Flux中
            System.out.println("emitter = " + emitter);

            eventCache.forEach(emitter::next);
        });
    }

    @GetMapping("/send")
    public void send(String data) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", LocalDate.now());
        map.put("data", data);
        DataChangeEvent event = new DataChangeEvent(map);
        eventPublisher.publishEvent(event);
    }


}
