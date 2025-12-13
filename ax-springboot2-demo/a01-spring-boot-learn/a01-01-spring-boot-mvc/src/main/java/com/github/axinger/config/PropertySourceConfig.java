package com.github.axinger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration
public class PropertySourceConfig {

    @Bean
    public CustomPropertySource customPropertySource() {
        return new CustomPropertySource();
    }

    @Bean
    public boolean registerPropertySource(ConfigurableEnvironment environment) {
        environment.getPropertySources().addAfter(
                "random",  // 在random属性源之后添加
                new CustomPropertySource()
        );
        return true;
    }
}
