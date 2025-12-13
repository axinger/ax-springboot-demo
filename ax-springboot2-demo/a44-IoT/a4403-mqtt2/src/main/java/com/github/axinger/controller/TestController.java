package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.dromara.mica.mqtt.spring.client.MqttClientTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
public class TestController {


    @Autowired
    private MqttClientTemplate client;

    @GetMapping(value = "/1")
    public boolean publish() {
        client.publish("/test/client", "mica最牛皮".getBytes(StandardCharsets.UTF_8));
        return true;
    }

//    public boolean sub() {
//        client.subQos0("/test/#", (context, topic, message, payload) -> {
//            log.info(topic + '\t' + new String(payload, StandardCharsets.UTF_8));
//        });
//        return true;
//    }
}
