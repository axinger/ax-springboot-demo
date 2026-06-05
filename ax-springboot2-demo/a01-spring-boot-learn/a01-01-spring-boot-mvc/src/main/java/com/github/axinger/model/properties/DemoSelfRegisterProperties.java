package com.github.axinger.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自行注册为 Bean 的 ConfigurationProperties 演示类
 * 通过 @Component 让组件扫描自动发现并注册为 Spring Bean
 * 对比：@EnableConfigurationProperties 方式需要显式在配置类中声明
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo.self")
public class DemoSelfRegisterProperties {

    /**
     * 用户名
     */
    private String username;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
