package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.github.axinger.mapper")
@SpringBootApplication
public class A3501FlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(A3501FlowableApplication.class, args);
    }
}
