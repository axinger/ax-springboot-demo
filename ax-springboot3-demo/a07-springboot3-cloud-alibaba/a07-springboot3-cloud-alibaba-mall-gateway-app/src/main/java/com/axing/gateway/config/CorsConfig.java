package com.axing.gateway.config;// package com.ax.service.gateway.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.reactive.CorsWebFilter;
// import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
// import org.springframework.web.util.pattern.PathPatternParser;
//
/// **
// * @author xing
// * @version 1.0.0
// * @ClassName CrosConfig.java
// * @Description TODO
// * @createTime 2022年01月31日 22:56:00
// */
//
//@Configuration
// public class CorsConfig {
//
//    @Bean
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedMethod("*");//支持所有方法
//        config.addAllowedOrigin("*");//跨域处理 允许所有的域
//        config.addAllowedHeader("*");//支持所有请求头
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);//匹配所有请求
//        return new CorsWebFilter(source);
//    }
//
//}
