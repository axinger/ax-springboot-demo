package com.github.axinger.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RedisRateLimitingFilter> loggingFilter() {
        FilterRegistrationBean<RedisRateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RedisRateLimitingFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
