package com.github.axinger.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LMConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String key;

    @Bean
    public DashScopeApi dashScopeApi() {
        return DashScopeApi.builder()
                .apiKey(key)
                .build();
    }


    @Bean("qwen")
    public ChatModel chatModel() {
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi())
                .defaultOptions(DashScopeChatOptions.builder().model("qwen-plus").build())
                .build();
    }

    @Bean("qwenChatClient")
    public ChatClient dashScopeChatClient(@Qualifier("qwen") ChatModel dashScopeModel,
                                          ChatMemoryRepository jedisRedisChatMemoryRepository) {

        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(jedisRedisChatMemoryRepository)
                .maxMessages(10)
                .build();

        return ChatClient.builder(dashScopeModel)
                /// 指定模型
                .defaultOptions(DashScopeChatOptions.builder().model("qwen-plus").build())
                /// 顾问增强器
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }
}
