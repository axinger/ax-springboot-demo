package com.github.axinger.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TtlAutoProxyConfig {

//    @Bean
//    public static BeanPostProcessor ttlThreadPoolPostProcessor() {
//        return new BeanPostProcessor() {
//            @Override
//            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
//                if (bean instanceof ThreadPoolTaskExecutor executor) {
//                    // 如果已经设置了装饰器，可能需要组合，这里简单覆盖或判断
//                    // 建议：如果用户没设，则设置；如果设了，用 TtlCompositeDecorator 包裹
//                    executor.setTaskDecorator(new TtlTaskDecorator());
//                }
//                return bean;
//            }
//        };
//    }
}
