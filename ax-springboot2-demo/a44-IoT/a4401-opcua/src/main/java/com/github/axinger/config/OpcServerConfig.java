package com.github.axinger.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName OpcServerConfig.java
 * description opc 服务信息
 * @createTime 2022年05月12日 16:20:00
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "opc")
public class OpcServerConfig {

    private String id;

    private String url;

    private String username;

    private String password;
}
