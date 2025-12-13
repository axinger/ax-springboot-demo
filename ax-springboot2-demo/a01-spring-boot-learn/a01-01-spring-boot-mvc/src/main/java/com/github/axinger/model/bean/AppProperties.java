package com.github.axinger.model.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "my.app")
public class AppProperties {
    private boolean enabled;
    private String mode;
    private List<String> features;

}
