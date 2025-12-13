package com.axing.common.quartz.config;

import com.axing.common.quartz.service.JobService;
import com.axing.common.quartz.service.impl.JobServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzAutoConfig {

    @Bean
    @ConditionalOnMissingBean(JobService.class)
    public JobService quartzService() {
        return new JobServiceImpl();
    }
}
