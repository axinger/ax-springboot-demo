package com.github.axinger.config;

import com.github.axinger.service.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    /**
     * 将工具方法暴露给外部的mcp client 调用
     */

    @Bean
    public ToolCallbackProvider weatherTools(WeatherService weatherService){

        return MethodToolCallbackProvider.builder()
                .toolObjects(weatherService)
                .build();
    }
}
