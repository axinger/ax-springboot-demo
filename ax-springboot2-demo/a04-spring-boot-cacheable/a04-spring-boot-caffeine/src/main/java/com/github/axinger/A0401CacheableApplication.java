package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class A0401CacheableApplication {
    public static void main(String[] args) {
        SpringApplication.run(A0401CacheableApplication.class, args);
    }
}
