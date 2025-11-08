package com.github.axinger.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// class 不要单独使用 @Configuration  ,不然无整体返回给webmvc,但是可以正常取值,但可以属性一个个获取, 统一使用 EnableConfigurationProperties
@Slf4j
@Data
@Component
//@Configuration
@RefreshScope
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "axinger.user")
public class SysUser {
    private String name;
    private Integer age;

    private String fullName;

    @PostConstruct
    public void init() {
        log.info("User初始化 name={},age={}", name, age);
        this.fullName = name + " " + age;
    }
}
