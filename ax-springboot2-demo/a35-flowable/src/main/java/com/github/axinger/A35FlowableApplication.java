package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.github.axinger.mapper")
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class A35FlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(A35FlowableApplication.class, args);
    }
}
