package com.github.axinger.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    @Resource(name = "ollamaChatModel")
    private ChatModel chatModel;


    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.call(msg);
    }

    @GetMapping("/test2")
    public Flux<String> test2(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.stream(msg);
    }

}
