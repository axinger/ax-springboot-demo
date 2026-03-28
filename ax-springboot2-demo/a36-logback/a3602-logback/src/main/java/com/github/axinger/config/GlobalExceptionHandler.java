package com.github.axinger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author xing
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleException(Exception e) {
        log.error("发生异常: {}", e.getMessage(), e);

        Map<String, Object> result = new HashMap<>();
        result.put("error", e.getMessage());
        result.put("success", false);
        return result;
    }
}
