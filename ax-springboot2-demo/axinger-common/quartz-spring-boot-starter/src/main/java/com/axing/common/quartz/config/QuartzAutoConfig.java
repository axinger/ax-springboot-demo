package com.axing.common.quartz.config;

import com.axing.common.quartz.service.QuartzTemplate;
import com.axing.common.quartz.service.impl.QuartzTemplateImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzAutoConfig {

    @Bean
    @ConditionalOnMissingBean(QuartzTemplate.class)
    public QuartzTemplate quartzService() {
        return new QuartzTemplateImpl();
    }
}
