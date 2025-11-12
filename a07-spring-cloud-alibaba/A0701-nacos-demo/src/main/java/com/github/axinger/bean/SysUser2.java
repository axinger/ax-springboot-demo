package com.github.axinger.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@Configuration
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
public class SysUser2 {

    @Value("${axinger.user.name}")
    private String name;


    @Value("${axinger.user.age}")
    private Integer age;


    private String fullName;

    @PostConstruct
    public void init() {
        log.info("User2初始化 name={},age={}", name, age);
        this.fullName = name + " " + age;
    }
}
