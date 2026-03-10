package com.github.axinger.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 有个默认在 dashScopeChatModel
 */
@Configuration
public class LMConfig {
//
//
//    @Value("${spring.ai.dashscope.api-key}")
//    private String key;
//
//    @Bean("deepseekChatModel")
//    public ChatModel deepseekChatModel() {
//        return DashScopeChatModel.builder()
//                .dashScopeApi(DashScopeApi.builder().apiKey(key)
//                        .build())
//                .defaultOptions(DashScopeChatOptions.builder()
//                        .model("deepseek-v3")
//                        .build())
//                .build();
//    }
//
//    @Bean
//    public ChatClient dashScopeChatClient(@Qualifier("dashScopeChatModel") ChatModel dashScopeModel) {
//        return ChatClient.builder(dashScopeModel).build();
//    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

}
