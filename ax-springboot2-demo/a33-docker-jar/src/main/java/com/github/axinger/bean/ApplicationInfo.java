package com.github.axinger.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.application")
public class ApplicationInfo {
    private String version;
    private String description;
    private Integer debug;
}
