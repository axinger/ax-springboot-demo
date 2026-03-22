package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.LocalDateTime;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName AppApplication.java
 * @Description TODO
 * @createTime 2022年02月12日 22:29:00
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class Demo6Application {
    public static void main(String[] args) {
        SpringApplication.run(Demo6Application.class, args);
        log.info("Demo6 info 日志 = {}", LocalDateTime.now());
    }
}
