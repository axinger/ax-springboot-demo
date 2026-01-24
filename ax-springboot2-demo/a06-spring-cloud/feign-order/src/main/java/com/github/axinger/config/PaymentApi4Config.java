package com.github.axinger.config;

import cn.hutool.core.util.ObjUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.api.PaymentApi4;
import com.github.axinger.dto.PaymentApiDTO;
import feign.Logger;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class PaymentApi4Config {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public RequestInterceptor paymentApi4RequestInterceptor(PaymentApi4 paymentApi4) {
        return template -> {
            template.header("token", "b123");

            PaymentApiDTO.LoginVO loginVO = paymentApi4.login(new PaymentApiDTO.LoginDTO());
            String token = loginVO.getToken();
            template.header("Authorization", "Bearer " + token);

//            template.target("动态设置url");
            {
                /// 获取查询参数
                Map<String, Collection<String>> queries = template.queries();
                String userId = Optional.ofNullable(queries.get("userId")).map(c -> c.iterator().next()).orElse(null);
                log.info("get请求参数,userId={}", userId);
            }


            // 获取请求体
            byte[] body = template.body();
            if (ObjUtil.isNotNull(body)) {
                try {
                    // 解析JSON请求体
                    JsonNode jsonNode = objectMapper.readTree(body);
                    // 获取某个字段的值
                    String userId = jsonNode.get("userId").asText();
                    log.info("post请求参数,userId={}", userId);
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
