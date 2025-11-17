package com.github.axinger.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "my-user")
public class MyUserProperties {
    private String username="jim";
    private String password;
    private List<String> tip;

    private List<Dog> dog;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dog {
        private String name="dog";
    }
}
