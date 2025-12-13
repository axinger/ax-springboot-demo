package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@AllArgsConstructor
@ConfigurationProperties(prefix = "auth-config")
public class AuthProperties {

    private List<String> matchPathList;
    private List<String> notMatchPathList;

}
