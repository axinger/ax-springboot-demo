package com.github.axinger.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Slf4j
@Data
@RefreshScope//实时更新
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "axinger.user5")
public class SysUser5 {
    private String name;
    private Integer age;
}
