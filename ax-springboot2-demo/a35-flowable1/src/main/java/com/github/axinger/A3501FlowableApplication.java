package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class A3501FlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(A3501FlowableApplication.class, args);
    }
}