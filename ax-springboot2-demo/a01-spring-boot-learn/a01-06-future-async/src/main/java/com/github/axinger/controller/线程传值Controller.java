package com.github.axinger.controller;

import com.github.axinger.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

@Slf4j
@RestController
@RequestMapping("/demo1")
public class 线程传值Controller {


    @Autowired
    @Qualifier("smsExecutor") // 使用配置了 TTL 装饰器的线程池
    private Executor executor;


    @GetMapping("/test1")
    public void createOrder() {
        // 主线程设置
        UserContext.setUserId("user_123");
        log.info("异步线程中的用户ID:主线程={}", UserContext.getUserId());
        // 异步执行
        executor.execute(() -> {
            // 【关键点】这里能打印出 "user_123"，如果是普通 ThreadLocal 则为 null
            log.info("异步线程中的用户ID:={}", UserContext.getUserId());

        });
    }
}
