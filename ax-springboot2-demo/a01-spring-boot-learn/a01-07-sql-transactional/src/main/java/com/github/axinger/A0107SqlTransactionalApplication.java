package com.github.axinger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.github.axinger.mapper")
public class A0107SqlTransactionalApplication {

    public static void main(String[] args) {
        SpringApplication.run(A0107SqlTransactionalApplication.class, args);
    }
}
