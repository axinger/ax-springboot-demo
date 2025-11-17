package com.github.axinger.config;

import com.github.axinger.model.UserConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        UserConfig.class
})
public class MyAutoImportConfig {

}
