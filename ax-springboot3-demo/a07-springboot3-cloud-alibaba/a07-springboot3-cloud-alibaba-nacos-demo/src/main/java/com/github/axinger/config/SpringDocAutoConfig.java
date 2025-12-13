package com.github.axinger.config;

import com.github.axinger.bean.DocInfoProperties;
import com.github.axinger.bean.PersonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope // 支持nacos的动态刷新功能
@EnableConfigurationProperties({
        DocInfoProperties.class,
        PersonProperties.class
})
public class SpringDocAutoConfig {


}
