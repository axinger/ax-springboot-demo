package com.github.axinger.model.bean;

import com.axing.common.util.factory.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Data
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "my")
@PropertySource(value = {"classpath:my.yml"}, factory = YamlPropertySourceFactory.class) // 需要自定义yaml解析

public class MyYmlBean {

    private MyYmlBean.User user;

    private List<MyYmlBean.User> list;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String username;
        private String password;
        private List<String> tip;
    }
}
