package com.github.axinger;

import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = {"com.github.axinger.model", "com.github.axinger.service"})
//@EnableCreateCacheAnnotation
public class A0405DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(A0405DemoApplication.class, args);
    }
}
