package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Slf4j
class A36LogbackApplicationTest {

    @Autowired
    private LoginService loginService;

    @Test
    void test1() {
        log.error("error = {}", "error 日志");
        log.info("info = {}", "info 日志");
        log.debug("debug = {}", "debug 日志");
        log.warn("info = {}", "warn 日志");
    }
}
