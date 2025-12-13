package com.github.axinger.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
@Slf4j
public class A {
    @Scheduled(cron = "0/5 * 20-21 * * 5")
    public void test121() {
        log.info("test1={}", LocalDateTime.now());
    }
}
