package com.github.axinger.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {


    /// 使用了mcp配置
    @Resource
    private ChatClient chatClient;

    /// 没有使用了mcp配置
    @Resource
    private ChatModel chatModel;


    @GetMapping("/test1")
    public Flux<String> test1(@RequestParam(name = "msg", defaultValue = "北京天气") String msg) {
        return chatClient.prompt(msg)
                .stream()
                .content();
    }

    @GetMapping("/test2")
    public Flux<String> test2(@RequestParam(name = "msg", defaultValue = "北京天气") String msg) {
        return chatModel.stream(msg);
    }


}
