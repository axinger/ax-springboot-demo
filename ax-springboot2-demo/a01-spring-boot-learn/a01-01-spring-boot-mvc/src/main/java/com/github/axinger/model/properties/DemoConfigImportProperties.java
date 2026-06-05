package com.github.axinger.model.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * spring.config.import 外部文件加载演示
 * 配置通过 spring.config.import 引入的独立 YAML 文件加载
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "demo.config")
public class DemoConfigImportProperties {
    private String username;
    private String password;
    private List<String> tip;
    private List<Dog> dog;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dog {
        private String name = "dog";
    }
}
