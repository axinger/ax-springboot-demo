package com.axing.common.advice.config;

import com.axing.common.advice.bean.AdviceProperties;
import com.axing.common.advice.exception.GlobalException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AdviceProperties.class)
// @PropertySource(value = "classpath:axing.advice.dispose.properties", encoding = "UTF-8")
public class AdviceAutoConfig {

    @Bean
    public GlobalException globalException(AdviceProperties adviceProperties) {
        return new GlobalException(adviceProperties);
    }

//    @Bean
//    public GlobalResponse globalResponse(AdviceProperties adviceProperties) {
//        return new GlobalResponse(adviceProperties);
//    }

}
