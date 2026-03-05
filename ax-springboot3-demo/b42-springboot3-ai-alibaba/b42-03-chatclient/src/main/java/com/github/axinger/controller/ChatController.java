package com.github.axinger.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    //    @Resource(name="dashScopeChatModel")
//    @Resource(name="ollamaChatModel")
    @Autowired
    @Qualifier("dashScopeChatModel")
    private ChatModel chatModel;

    /// 不能自动注入,只能手动注入 ChatClient.builder
    @Resource
    private ChatClient chatClient;


    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.call(msg);
    }

    @GetMapping("/test11")
    public String test11(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatClient.prompt()
                .user(msg)
                .call()
                .content();
    }

    @GetMapping("/test2")
    public Flux<String> test2(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.stream(msg);
    }

    @GetMapping("/test21")
    public Flux<String> test21(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatClient.prompt()
                .user(msg)
                .stream()
                .content();
    }
}
