package com.github.axinger.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    @Value("${spring.profiles.active}")
    String My_Env;

    @GetMapping
    public void log1() {
        log.info("环境变量={}", My_Env);
        log.error("环境变量={}", My_Env);
        log.debug("环境变量={}", My_Env);
        log.warn("环境变量={}", My_Env);
        log.trace("环境变量={}", My_Env);
    }
}
