package com.github.axinger.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LMConfig {

    //    @Value("${spring.ai.dashscope.api-key}")
//    private String key;
//
//    @Bean
//    public DashScopeApi dashScopeApi() {
//        return DashScopeApi.builder()
////                .apiKey(key)
//                .apiKey(System.getenv("aliQwen-api"))
//                .build();
//    }


    /// 自定义多个ChatModel
//    @Bean
//    public ChatModel chatModel() {
//        return DashScopeChatModel.builder()
//                .dashScopeApi(dashScopeApi())
//                .defaultOptions(DashScopeChatOptions.builder()
//                        .model("qwen-plus")
//                        .build())
//                .build();
//    }

}
