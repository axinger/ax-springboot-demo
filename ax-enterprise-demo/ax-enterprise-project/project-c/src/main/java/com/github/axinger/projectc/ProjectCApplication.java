package com.github.axinger.projectc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目C启动类 - All-in-One 聚合服务
 *
 * 同时聚合了 project-a（用户服务）和 project-b（订单服务）的所有业务组件。
 * 通过 scanBasePackages 扩大扫描范围，确保能加载 project-a 和 project-b 中的 Bean。
 */
@SpringBootApplication(scanBasePackages = {
        "com.github.axinger.projecta",
        "com.github.axinger.projectb",
        "com.github.axinger.projectc"
})
public class ProjectCApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectCApplication.class, args);
    }
}
