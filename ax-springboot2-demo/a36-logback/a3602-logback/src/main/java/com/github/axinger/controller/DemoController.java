package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 演示 Controller - 展示 MDC 日志效果
 *
 * @author xing
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private Executor executor;

    @GetMapping("/test1")
    public Map<String, Object> test() {
        log.info("请求处理完成111");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info("请求处理完成222");
        }, executor);

        future.join();
        Map<String, Object> result = new HashMap<>();
        result.put("message", "success");
        return result;
    }


    @GetMapping("/error")
    public void error() {
        log.info("即将抛出异常");
        throw new RuntimeException("测试异常日志");
    }
}
