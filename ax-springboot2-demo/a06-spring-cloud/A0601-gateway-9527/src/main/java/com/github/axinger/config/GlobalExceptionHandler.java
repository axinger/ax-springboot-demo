package com.github.axinger.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(-1)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置响应头
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("timestamp", System.currentTimeMillis());

        // 处理不同类型的异常
        if (isRateLimitException(ex)) {
            response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            result.put("code", 429);
            result.put("message", "请求过于频繁，请稍后再试");
            log.error("限流触发: {} - {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI().getPath());
        } else if (ex instanceof NotFoundException) {
            response.setStatusCode(HttpStatus.NOT_FOUND);
            result.put("code", 404);
            result.put("message", "服务未找到");
        } else if (ex instanceof ResponseStatusException) {
            ResponseStatusException statusException = (ResponseStatusException) ex;
            response.setStatusCode(statusException.getStatus());
            result.put("code", statusException.getStatus().value());
            result.put("message", statusException.getMessage());
        } else {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            result.put("code", 500);
            result.put("message", "系统内部错误");
            log.error("网关异常: ", ex);
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                byte[] bytes = objectMapper.writeValueAsBytes(result);
                return bufferFactory.wrap(bytes);
            } catch (JsonProcessingException e) {
                log.error("序列化响应结果失败", e);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

    /**
     * 判断是否为限流异常
     * 由于Spring Cloud Gateway的限流异常类型可能变化，通过消息内容判断
     */
    private boolean isRateLimitException(Throwable ex) {
        String message = ex.getMessage();
        if (message == null) {
            return false;
        }

        // 检查异常消息中是否包含限流相关的关键词
        return message.contains("Unable to subscribe to backpressure executor") ||
               message.contains("RateLimiter") ||
               message.contains("Too Many Requests") ||
               message.contains("429");
    }
}
