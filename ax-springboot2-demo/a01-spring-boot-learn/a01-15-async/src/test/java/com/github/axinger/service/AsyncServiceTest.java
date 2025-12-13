package com.github.axinger.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class AsyncServiceTest {


    @Test
    @Async("customizeThreadPool")
    public void test11() {
        int sleepSeconds = new Random().nextInt(3) + 1;
        try {
            TimeUnit.SECONDS.sleep(sleepSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test11 currentThread = " + Thread.currentThread().getName());
    }
}
