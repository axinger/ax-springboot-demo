package com.github.axinger.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {


    private final ImageModel imageModel;
    @Resource(name = "dashScopeChatModel")
    private ChatModel chatModel;
    @Resource(name = "qwenChatModel")
    private ChatModel qwenChatModel;

    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.call(msg);
    }

    @GetMapping("/test2")
    public Flux<String> test2(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.stream(msg);
    }

    @GetMapping("/test3")
    public String test3(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return qwenChatModel.call(msg);
    }
}
