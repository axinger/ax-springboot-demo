package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Async
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class A0203DynamicAtomikosApplication {

    public static void main(String[] args) {
        SpringApplication.run(A0203DynamicAtomikosApplication.class, args);
    }

}
