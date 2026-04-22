package axing.cloud.gateway.config;

import axing.cloud.gateway.bean.FetchGatewayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@EnableConfigurationProperties(FetchGatewayProperties.class)
public class FetchGatewayAutoConfigure {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FetchGatewayInterceptorConfigurer cloudSecurityInterceptorConfigure(FetchGatewayProperties properties) {
        return new FetchGatewayInterceptorConfigurer(properties);
    }

}

