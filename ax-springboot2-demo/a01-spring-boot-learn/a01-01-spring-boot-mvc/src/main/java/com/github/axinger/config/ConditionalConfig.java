package com.github.axinger.config;

import com.github.axinger.model.properties.DemoProperties;
import com.github.axinger.model.properties.MyBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DemoProperties.class)
public class ConditionalConfig {

    @Bean("myBean1")
    @Conditional(MyCondition.class)
    public MyBean myBean1(DemoProperties demoProperties) {
        return new MyBean(demoProperties);
    }

    @Bean("myBean2")
    @Conditional(MyCondition2.class)
    public MyBean myBean2(DemoProperties demoProperties) {
        return new MyBean(demoProperties);
    }

    @Bean("myBean3")
    @ConditionalOnExpression("#{environment['spring.profiles.active'] == 'dev' && systemProperties['user.country'] == 'CN'}")
    public MyBean myBean3(DemoProperties demoProperties) {
        return new MyBean(demoProperties);
    }

    @Bean("myBean4")
    @ConditionalOnProperty(name = "user.show", havingValue = "true")
    public MyBean myBean4(DemoProperties demoProperties) {
        return new MyBean(demoProperties);
    }

    @Bean("myBean5")
    @ConditionalOnCloudPlatform(CloudPlatform.SAP)
    public MyBean myBean5(DemoProperties demoProperties) {
        return new MyBean(demoProperties);
    }
}
