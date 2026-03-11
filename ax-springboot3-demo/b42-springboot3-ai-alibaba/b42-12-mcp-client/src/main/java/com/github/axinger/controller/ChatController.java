package com.github.axinger.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {


    @Resource
    private ChatClient chatClient;///使用了mcp配置


    @GetMapping("/test1")
    public Flux<String> test1(@RequestParam(name = "msg", defaultValue = "北京") String msg) {
        return chatClient.prompt(msg)
                .stream()
                .content();

    }


}
