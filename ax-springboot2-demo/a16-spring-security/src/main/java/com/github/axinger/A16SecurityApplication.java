package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = "com.github.axinger.mapper")
@SpringBootApplication
public class A16SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(A16SecurityApplication.class, args);
    }
}
