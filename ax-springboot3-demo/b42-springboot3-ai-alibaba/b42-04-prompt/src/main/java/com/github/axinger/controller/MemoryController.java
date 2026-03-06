package com.github.axinger.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryController {

    @Resource
    @Qualifier("dashScopeChatModel")
    private ChatModel dashScopeChatModel;

    @Resource
    @Qualifier("dashScopeChatClient")
    private ChatClient dashScopeChatClient;
}
