package axing.cloud.gateway.config;

import axing.cloud.gateway.bean.FetchGatewayProperties;
import axing.cloud.gateway.handler.FetchGatewayInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class FetchGatewayInterceptorConfigurer implements WebMvcConfigurer {

    private final FetchGatewayProperties properties;

    public FetchGatewayInterceptorConfigurer(FetchGatewayProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        FetchGatewayInterceptor interceptor = new FetchGatewayInterceptor(properties);
        registry.addInterceptor(interceptor).order(-1);
    }
}

