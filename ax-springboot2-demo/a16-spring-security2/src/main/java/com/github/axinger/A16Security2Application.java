package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.github.axinger.mapper")
@SpringBootApplication
public class A16Security2Application {
    public static void main(String[] args) {
        SpringApplication.run(A16Security2Application.class, args);
    }
}
