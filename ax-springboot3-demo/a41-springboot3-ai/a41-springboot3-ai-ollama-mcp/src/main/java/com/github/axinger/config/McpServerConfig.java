package com.github.axinger.config;

import com.github.axinger.tool.StudentTool;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * MCP服务器配置类，负责注册MCP工具
 */
@Configuration
public class McpServerConfig {

    @Bean
    @Primary // 添加此注解指定优先使用此Bean
    public ToolCallbackProvider toolProvider(StudentTool studentTool) {
        // 注册工具类实例
        ToolCallback[] callbacks = ToolCallbacks.from(studentTool);
        return ToolCallbackProvider.from(callbacks);
    }

}
