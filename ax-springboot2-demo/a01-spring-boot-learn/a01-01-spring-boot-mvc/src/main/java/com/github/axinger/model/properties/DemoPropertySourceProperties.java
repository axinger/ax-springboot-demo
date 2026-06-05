package com.github.axinger.model.properties;

import com.axing.common.util.factory.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @PropertySource 外部文件加载演示
 * 通过自定义 YamlPropertySourceFactory 加载非 application 的 YAML 文件
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "demo.source")
@PropertySource(value = {"classpath:demo-source.yml"}, factory = YamlPropertySourceFactory.class)
public class DemoPropertySourceProperties {

    private User user;
    private List<User> list;

    @Data
    public static class User {
        private String username;
        private String password;
        private List<String> tip;
    }
}
