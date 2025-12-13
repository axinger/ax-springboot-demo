package com.github.axinger.config;

import com.github.axinger.model.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        AuthProperties.class
})
public class MyAutoImportConfig {

}
