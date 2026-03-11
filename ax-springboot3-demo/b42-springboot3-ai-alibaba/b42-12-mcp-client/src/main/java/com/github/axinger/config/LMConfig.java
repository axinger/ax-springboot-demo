package com.github.axinger.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 有个默认在 dashScopeChatModel
 */
@Configuration
public class LMConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider toolCallbackProvider) {
        return ChatClient.builder(chatModel)
                .defaultToolCallbacks(toolCallbackProvider.getToolCallbacks())///  mcp协议,配置见yml
                .build();
    }

}
