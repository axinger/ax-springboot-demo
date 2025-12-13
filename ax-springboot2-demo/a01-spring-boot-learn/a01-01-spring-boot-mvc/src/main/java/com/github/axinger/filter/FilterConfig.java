package com.github.axinger.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

//    @Autowired
//    private OrderFilterOnce orderFilterOnce;
//
//    @Bean
//    public FilterRegistrationBean<OrderFilterOnce> myOnceFilterFilterRegistrationBean() {
//        FilterRegistrationBean<OrderFilterOnce> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(orderFilterOnce);
//        registrationBean.addUrlPatterns("/order/*"); // 指定拦截路径
//        registrationBean.setOrder(1); // 设置过滤器顺序
//        return registrationBean;
//    }

    //    @Autowired
//    TestFilter testFilter;
//
    @Bean
    public FilterRegistrationBean<TestFilter> testFilterRegistration(TestFilter testFilter) {
        FilterRegistrationBean<TestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(testFilter);
        registration.addUrlPatterns("/test/*");
        registration.setOrder(-1);
        return registration;
    }


//    @Bean
//    public FilterRegistrationBean<OrderFilter> orderFilterFilterRegistrationBean(OrderFilter orderFilter) {
//        FilterRegistrationBean<OrderFilter> registration = new FilterRegistrationBean<>();
//        registration.setFilter(orderFilter);
//        registration.addUrlPatterns("/order/*"); // 不拦截 /order/**
//        registration.setOrder(-2);
//        return registration;
//    }
}
