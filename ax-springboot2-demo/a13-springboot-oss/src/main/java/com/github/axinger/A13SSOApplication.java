package com.github.axinger;


import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFileStorage
@MapperScan(value = {"com.github.axinger.mapper"})
@SpringBootApplication
//@ComponentScan(basePackages = {"com.axing.common.response", "com.axing.common.util", "com.github.axinger"})
public class A13SSOApplication {

    public static void main(String[] args) {
        SpringApplication.run(A13SSOApplication.class, args);
    }

}
