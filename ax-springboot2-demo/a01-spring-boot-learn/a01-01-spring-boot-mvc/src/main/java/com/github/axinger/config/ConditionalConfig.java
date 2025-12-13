package com.github.axinger.config;

import com.github.axinger.model.bean.AppProperties;
import com.github.axinger.model.bean.MyBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class ConditionalConfig {

//    @ConditionalOnProperty(name = "my.feature.enabled", havingValue = "true")
//    @ConditionalOnProperty(prefix = "my.app.cache", name = "preload", havingValue = "true")
//    @ConditionalOnExpression("${my.module.enabled:false} && ${my.module.mode} == 'advanced'")


    @Bean("myBean1")
    @Conditional(MyCondition.class)
    public MyBean myBean1(AppProperties appProperties) {
        return new MyBean(appProperties);
    }

    @Bean("myBean2")
    @Conditional(MyCondition2.class)
    public MyBean myBean2(AppProperties appProperties) {
        return new MyBean(appProperties);
    }

    @Bean("myBean3")
//    @ConditionalOnExpression("${my.module.enabled:false} && ${my.module.mode} == 'advanced'")
    @ConditionalOnExpression("#{environment['spring.profiles.active'] == 'dev' && systemProperties['user.country'] == 'CN'}")
    public MyBean myBean3(AppProperties appProperties) {
        return new MyBean(appProperties);
    }

    @Bean("myBean4")
    @ConditionalOnProperty(name = "user.show", havingValue = "true")
    public MyBean myBean4(AppProperties appProperties) {
        return new MyBean(appProperties);
    }

    @Bean("myBean5")
    @ConditionalOnCloudPlatform(CloudPlatform.SAP)
    public MyBean myBean5(AppProperties appProperties) {
        return new MyBean(appProperties);
    }
}
