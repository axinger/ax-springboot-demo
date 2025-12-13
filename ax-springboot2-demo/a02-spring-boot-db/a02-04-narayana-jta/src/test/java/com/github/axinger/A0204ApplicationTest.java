package com.github.axinger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@Slf4j
class A0204ApplicationTest {

    @Autowired
    private Executor executor;

    @SneakyThrows
    @Test
    void contextLoads() {

//        CompletableFuture.runAsync(() -> {
//
//         log.info("Thread.currentThread().getId() = {}" , Thread.currentThread().getName());
//        },executor);
        contextLoads2();
        TimeUnit.SECONDS.sleep(2);
    }


    @Async("abc")
    void contextLoads2() {
        log.info("Thread.currentThread().getId() = {}", Thread.currentThread().getName());
    }
}
