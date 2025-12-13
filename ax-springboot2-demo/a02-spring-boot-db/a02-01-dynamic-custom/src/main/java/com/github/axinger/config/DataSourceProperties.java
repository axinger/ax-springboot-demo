package com.github.axinger.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.datasource.dynamic")
public class DataSourceProperties {
    private Map<String, DataSourceItem> datasource = new HashMap<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Configuration
    public static class DataSourceItem {
        int xaMinPoolSize = 1;
        int xaMaxPoolSize = 1;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private String xaDriverClassName;

        private List<String> mapperLocations;
    }
}
