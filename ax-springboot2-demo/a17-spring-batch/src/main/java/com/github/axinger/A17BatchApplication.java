package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude = {BatchAutoConfiguratio.class})
@MapperScan("com.github.axinger.mapper")
public class A17BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(A17BatchApplication.class, args);
    }
}
