package com.github.axinger.config;

import com.github.axinger.entity.Pig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public Pig pig() {
        Pig pig = new Pig();
        System.out.println("pig = " + pig);
        return pig;
    }
}
