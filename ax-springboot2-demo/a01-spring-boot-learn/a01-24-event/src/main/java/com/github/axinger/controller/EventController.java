package com.github.axinger.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.github.axinger.event.CustomEvent;
import com.github.axinger.event.EventProducer;
import com.github.axinger.service.IMyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@Validated
public class EventController {

    @Autowired
    private IMyService iMyService;
    @Autowired
    private EventProducer<CustomEvent> eventProducer;
    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping("/test")
    public Object login(@NotEmpty(message = "name不能为空") String name) {
        CompletableFuture.runAsync(() -> {
            iMyService.test(name);
        });
        return List.of(name);
    }

    @GetMapping("/send")
    public Object send(int count) {
//        applicationEventPublisher.publishEvent(new CustomEvent(count));
        eventProducer.produceEvent(new CustomEvent(count));
        return List.of(count);
    }

    @GetMapping("/push")
    public Object add(String name) {
        publisher.publishEvent(new CustomEvent(name));
        return List.of(name);
    }

    @GetMapping("/push2")
    public Object push2(String name) {
        SpringUtil.publishEvent(new CustomEvent(name));
        return List.of(name);
    }

}
