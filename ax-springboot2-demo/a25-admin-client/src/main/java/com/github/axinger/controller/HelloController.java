package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName HelloController.java
 * @description TODO
 * @createTime 2022年06月15日 16:02:00
 */

/**
 * HelloController
 */
@Slf4j
@RestController
public class HelloController {
    private final AtomicInteger count = new AtomicInteger(0);

    @GetMapping("/hi")
    private String sayHi() {
        // 每次进来如打印下日志
        log.info("{} 啪...我第{}次进来了.", LocalDateTime.now(), count.addAndGet(1));
        // 每次进来new 个大对象，便于监控观察堆内存变化
        byte[] bytes = new byte[100 * 1024 * 1024];
        log.info("new了 100MB");
        return "hi springboot addmin " + LocalDateTime.now();
    }

    @GetMapping("/log")
    private void log() {
        log.info("info================");
        log.debug("debug================");
        log.error("error================");
        log.warn("warn================");
        log.trace("trace================");
    }
}
