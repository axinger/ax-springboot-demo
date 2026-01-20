package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/// [...](http://localhost:8888/axinger-user/default)
@SpringBootApplication
@EnableConfigServer
public class A0602ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(A0602ConfigServerApplication.class, args);
    }
}
