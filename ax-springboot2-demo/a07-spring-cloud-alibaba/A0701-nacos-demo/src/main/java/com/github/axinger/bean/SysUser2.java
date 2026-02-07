package com.github.axinger.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@Configuration
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
@Order(12)
public class SysUser2 {

    @Value("${axinger.user2.name}")
    private String name;


    @Value("${axinger.user2.age}")
    private Integer age;


    private String fullName;

    /// 这个方式,不能转json,也是懒加载,更新
    @PostConstruct
    public void init() {
        try {
            log.info("\n\n👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇");
//            log.info("User2初始化={}", JsonUtil.toJsonStr(this));
            log.info("User2初始化age={},name={}", this.age, this.name);
            log.info("\n👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆👆\n");
        } catch (Exception e) {
            log.error("User2初始化 error: {}", e.getMessage());
        }

        this.fullName = name + " " + age;
    }
}
