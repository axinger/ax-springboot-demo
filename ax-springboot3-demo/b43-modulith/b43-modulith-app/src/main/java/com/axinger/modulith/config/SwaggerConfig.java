package com.axinger.modulith.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 配置类
 * 提供 API 文档和交互式 API 浏览器
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Modulith 企业级电商平台 API")
                        .description("""
                            这是一个使用 Spring Modulith 构建的企业级模块化架构电商平台。

                            ## 功能模块
                            - **订单模块**: 订单创建、确认、取消和状态管理
                            - **客户模块**: 客户注册、信息管理和状态管理
                            - **库存模块**: 产品库存管理和库存操作
                            - **支付模块**: 支付处理和交易管理
                            - **通知模块**: 系统通知和消息发送

                            ## 技术栈
                            - Spring Boot 3.5+
                            - Spring Modulith 1.2.0
                            - Spring Data JPA
                            - H2 数据库
                            - Java 17

                            ## 快速开始
                            1. 启动应用
                            2. 访问 H2 控制台: /h2-console
                            3. 使用 API 文档测试接口
                            """)
                        .version("v1.0.0")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                        .servers(List.of(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("本地开发环境"),
                                new Server()
                                        .url("https://api.example.com")
                                        .description("生产环境")
                        ))
                        .externalDocs(new ExternalDocumentation()
                                .description("Spring Modulith 文档")
                                .url("https://docs.spring.io/spring-modulith/docs/current/reference/html/"))
                        .components(new Components()
                                .addSecuritySchemes("basicAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic"))
                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}