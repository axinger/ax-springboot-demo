package com.github.axinger.model.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        DemoProperties.class,
        DemoUserProperties.class,
        DemoBlockScalarProperties.class,
        DemoPropertySourceProperties.class,
        DemoConfigImportProperties.class
})
public class MyAutoImportConfig {
}
