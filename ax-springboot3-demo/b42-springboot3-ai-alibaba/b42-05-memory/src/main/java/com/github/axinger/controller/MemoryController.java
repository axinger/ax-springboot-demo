package com.github.axinger.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryController {

    @Resource
    @Qualifier("qwenChatClient")
    private ChatClient qwenChatClient;


    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg,
                        @RequestParam(name = "userId", defaultValue = "A001") String userId) {
        return qwenChatClient.prompt()
                .user(msg)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }
}
