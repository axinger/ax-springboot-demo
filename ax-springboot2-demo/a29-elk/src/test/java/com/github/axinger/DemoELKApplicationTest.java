package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
class DemoELKApplicationTest {


    @Test
    void elk_info() {
        log.info("DemoElk info 测试 = {}", LocalDateTime.now());
    }

    @Test
    void elk_error() {
        log.error("DemoElk error 日志 = {}", LocalDateTime.now());
    }
}
