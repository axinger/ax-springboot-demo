package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 资源服务器启动类
 */
@SpringBootApplication
@MapperScan("com.github.axinger.mapper")
public class Oauth2ResourceApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ResourceApplication.class, args);
    }
}
