package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class A14NettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(A14NettyApplication.class, args);
    }

}
