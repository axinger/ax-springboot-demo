package com.axing.common.json.auto;

import com.axing.common.json.config.Jackson2ObjectMapperConfig;
import com.axing.common.json.config.LocalDateFormatterConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({
        Jackson2ObjectMapperConfig.class,
        LocalDateFormatterConfig.class
})
public class MyJackson2ObjectAutoConfig {
}
