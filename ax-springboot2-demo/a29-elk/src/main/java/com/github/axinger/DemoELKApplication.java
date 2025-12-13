package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
@Slf4j
public class DemoELKApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoELKApplication.class, args);
//        log.info("DemoElk info 日志 = {}", LocalDateTime.now());
//        log.error("DemoElk error 日志 = {}", LocalDateTime.now());

        log.info("DemoElk info 测试 = {}", LocalDateTime.now());
    }
}
