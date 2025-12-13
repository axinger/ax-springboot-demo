package com.github.axinger.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "axinger.person")
public class Person {
    private String name;
    private Integer age;
}
