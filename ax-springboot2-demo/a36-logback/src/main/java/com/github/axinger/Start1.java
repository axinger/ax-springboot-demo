package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
@Slf4j
public class Start1 implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.error("error = {}", "error 日志");
        log.info("info = {}", "info 日志");
        log.debug("debug = {}", "debug 日志");
        log.warn("info = {}", "warn 日志");

        System.out.println("http://localhost:8080/actuator/health");
    }
}
