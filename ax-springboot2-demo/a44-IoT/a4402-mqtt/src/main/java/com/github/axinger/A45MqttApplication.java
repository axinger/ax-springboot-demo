package com.github.axinger;

import com.github.axinger.core.bean.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication

@EnableConfigurationProperties(MqttProperties.class)
public class A45MqttApplication {
    public static void main(String[] args) {
        SpringApplication.run(A45MqttApplication.class, args);
    }
}
