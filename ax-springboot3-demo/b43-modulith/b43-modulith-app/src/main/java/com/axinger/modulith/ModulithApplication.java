package com.axinger.modulith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Modulith 企业级应用主类
 *
 * 演示了如何使用 Spring Modulith 构建模块化架构的企业应用
 * 包含订单、客户、库存、支付、通知等多个业务模块
 */
@SpringBootApplication
@EnableScheduling  // 启用定时任务
@Modulith(
    systemName = "企业级电商平台",
    sharedModules = {"shared-kernel"},
    additionalPackages = {"com.axinger"}
)
public class ModulithApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModulithApplication.class, args);
    }
}