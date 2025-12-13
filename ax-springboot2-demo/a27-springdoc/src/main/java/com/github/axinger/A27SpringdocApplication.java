package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class A27SpringdocApplication {

    public static void main(String[] args) {
        SpringApplication.run(A27SpringdocApplication.class, args);
        log.debug("测试debug = {}", LocalDateTime.now());
        log.info("测试info = {}", LocalDateTime.now());
        log.warn("测试warn = {}", LocalDateTime.now());
        log.error("测试error = {}", LocalDateTime.now());
        log.error("测试error2 = {}", LocalDateTime.now());
        log.trace("测试trace = {}", LocalDateTime.now());
        log.info("swagger地址 = {}", "http://localhost:11027/swagger-ui/index.html");
    }

}
