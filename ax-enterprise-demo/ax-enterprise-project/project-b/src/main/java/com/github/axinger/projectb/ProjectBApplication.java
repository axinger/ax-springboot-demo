package com.github.axinger.projectb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目B启动类 - 订单服务
 * 可独立部署运行
 */
@SpringBootApplication
public class ProjectBApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectBApplication.class, args);
    }
}
