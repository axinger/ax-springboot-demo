package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.axinger.db.mapper")
public class A41Springboot3AiMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(A41Springboot3AiMcpApplication.class, args);
    }
}
