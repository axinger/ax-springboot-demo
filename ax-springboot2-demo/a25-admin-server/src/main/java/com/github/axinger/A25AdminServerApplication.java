package com.github.axinger;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer
public class A25AdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(A25AdminServerApplication.class, args);
    }
}
