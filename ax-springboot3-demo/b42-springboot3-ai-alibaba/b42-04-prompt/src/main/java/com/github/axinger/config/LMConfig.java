package com.github.axinger.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LMConfig {


    @Bean
    public ChatClient dashScopeChatClient(@Qualifier("dashScopeChatModel") ChatModel dashScopeModel) {
        return ChatClient.builder(dashScopeModel).build();
    }
}
