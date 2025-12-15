package com.github.axinger.config;

import com.github.axinger.tool.StudentTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 聊天客户端配置类
 */
@Configuration
public class ChatClientConfig {



    /**
     * 配置ChatClient，注册系统指令和工具函数
     */
    @Bean(name = "studentChatClient")
    @Primary
    public ChatClient studentChatClient(ChatModel chatModel, StudentTool studentTool) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个学生信息管理助手，可以帮助用户查询学生信息。" +
                        "你可以根据学生姓名模糊查询学生信息、根据条件分页查询学生信息。" +
                        "回复时，请使用简洁友好的语言，并将学生信息整理为易读的格式。")
                // 注册工具方法
                .defaultTools(studentTool)
                .build();
    }

}