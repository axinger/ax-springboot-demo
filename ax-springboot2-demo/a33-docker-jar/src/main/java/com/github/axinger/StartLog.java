package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
@Slf4j
public class StartLog implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) {
        log.error("普通 error = {}", "error 日志");
        log.info("普通 info = {}", "info 日志");
        log.debug("普通 debug = {}", "debug 日志");
        log.warn("普通 info = {}", "warn 日志");
    }
}
