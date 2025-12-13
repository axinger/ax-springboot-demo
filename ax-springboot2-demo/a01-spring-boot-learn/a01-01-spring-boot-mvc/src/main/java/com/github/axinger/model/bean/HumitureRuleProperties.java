package com.github.axinger.model.bean;


import org.springframework.boot.context.properties.ConfigurationProperties;

// @Configuration // record 不能使用
@ConfigurationProperties(prefix = "show")
public record HumitureRuleProperties(HumidityDto humidity, HumidityDto temperature) {

    public record HumidityDto(double min, double max) {
    }

}
