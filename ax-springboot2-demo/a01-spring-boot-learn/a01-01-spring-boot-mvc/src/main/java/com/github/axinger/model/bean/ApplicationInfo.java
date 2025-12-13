package com.github.axinger.model.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.application")
public record ApplicationInfo(String version, String description) {


}
