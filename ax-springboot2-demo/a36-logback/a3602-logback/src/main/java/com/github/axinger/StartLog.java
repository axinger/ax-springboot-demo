package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
@Slf4j
public class StartLog implements ApplicationRunner {

    // 第一个 logger：输出到 ORDER_LOG appender
    private static final Logger orderLog = LoggerFactory.getLogger("ORDER_LOG");

    // 第二个 logger：输出到 PAYMENT_LOG appender（你需要先配置 PAYMENT_LOG appender）
    private static final Logger paymentLog = LoggerFactory.getLogger("PAYMENT_LOG");

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.error("普通 error = {}", "error 日志");
        log.info("普通 info = {}", "info 日志");
        log.debug("普通 debug = {}", "debug 日志");
        log.warn("普通 info = {}", "warn 日志");

        System.out.println("ORDER_LOG========================================");
        orderLog.error("ORDER_LOG error = {}", "error 日志");
        orderLog.info("ORDER_LOG info = {}", "info 日志");
        orderLog.debug("ORDER_LOG debug = {}", "debug 日志");
        orderLog.warn("ORDER_LOG info = {}", "warn 日志");

        System.out.println("PAYMENT_LOG========================================");
        paymentLog.error("PAYMENT_LOG error = {}", "error 日志");
        paymentLog.info("PAYMENT_LOG info = {}", "info 日志");
        paymentLog.debug("PAYMENT_LOG debug = {}", "debug 日志");
        paymentLog.warn("PAYMENT_LOG info = {}", "warn 日志");

        System.out.println("http://localhost:8080/actuator/health");
    }
}
