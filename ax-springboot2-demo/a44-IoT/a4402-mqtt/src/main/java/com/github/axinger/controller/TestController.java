package com.github.axinger.controller;

import com.github.axinger.core.MqttTemplate;
import com.github.axinger.core.config.Topic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private MqttTemplate mqttTemplate;

    @GetMapping(value = "/1")
    public String test() {
        mqttTemplate.push(Topic.OPC_TOPIC, 1, "test");
        return "ok";
    }
}
