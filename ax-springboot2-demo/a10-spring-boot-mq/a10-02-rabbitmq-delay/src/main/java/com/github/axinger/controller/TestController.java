package com.github.axinger.controller;

import com.github.axinger.config.MyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MyProducer producer;

    @GetMapping("/delay/{flag}")
    public String send(@PathVariable String flag) {
        for (int i = 0; i < 5; i++) {
            producer.sendDelayedMessage(flag, "Hello from " + i + ">>>" + flag, 5000); // 延迟5秒
        }
        return "Sent!";
    }
}
