package com.github.axinger.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

//@Configuration /// 打开就是全局配置,关闭,就需要的自行配置
public class FeignClientConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("token", "b123");

            // 获取请求体
            byte[] body = template.body();
            if (body != null && body.length > 0) {
                try {
                    // 解析JSON请求体
                    JsonNode jsonNode = objectMapper.readTree(body);
                    // 获取某个字段的值
                    String orderId = jsonNode.get("orderId").asText();
                    // 根据字段值计算token
                    String orderId2 = "computed-token-based-on-" + orderId;
                    // 将新的字段添加到请求体
                    // 由于 ObjectNode 是 JsonNode 的子类
                    if (jsonNode instanceof ObjectNode node) {
                        node.put("orderId2", orderId2);
                        // 更新模板中的body为修改后的JSON字符串
                        template.body(objectMapper.writeValueAsString(node));
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to parse request body", e);
                }
            }

        };
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
