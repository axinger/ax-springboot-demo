package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * All-in-One 单体应用启动类
 * 集成授权服务器和资源服务器的功能，单进程运行
 */
@SpringBootApplication(scanBasePackages = "com.github.axinger")
@MapperScan("com.github.axinger.mapper")
public class Oauth2AllinoneApplication {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2AllinoneApplication.class, args);
    }
}
