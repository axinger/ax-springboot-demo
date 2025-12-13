package com.github.axinger.config;


import com.axing.starter.bean.HelloProperties;
import com.axing.starter.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableConfigurationProperties(HelloProperties.class)// 默认把HelloProperties 放入容器中
public class HelloService2AutoConfiguration {

    /**
     * 重写 ConditionalOnMissingBean
     *
     * @return
     */

    @Bean
//    @ConditionalOnMissingBean(HelloService.class)
    public HelloService helloService() {
        HelloService helloService = new HelloService2();
        log.info("重写HelloService={}", helloService);
        return helloService;
    }

}
