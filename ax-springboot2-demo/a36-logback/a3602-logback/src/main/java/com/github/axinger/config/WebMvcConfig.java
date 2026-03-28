package com.github.axinger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类 - 注册拦截器
 *
 * @author xing
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private MdcInterceptor mdcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除静态资源
                .excludePathPatterns("/static/**", "/favicon.ico", "/error");
    }
}
