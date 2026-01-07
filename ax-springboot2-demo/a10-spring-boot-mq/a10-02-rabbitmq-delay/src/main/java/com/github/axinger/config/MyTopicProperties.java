package com.github.axinger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "business")
public class MyTopicProperties {

    private List<TopicConfig> topics = new ArrayList<>();

    @Data
    public static class TopicConfig {
        private String flag;
    }
}
