package com.github.axinger.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

//@Component // ← 必须加！让 Spring 管理这个 Bean
@RefreshScope // 支持动态刷新
@ConfigurationProperties(prefix = "axinger.user5") // 前缀必须匹配
@Data
public class SysUser5 {
    private String name;
    private Integer age;
}
