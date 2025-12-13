//package com.github.axinger;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.boot.autoconfigure.AutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.hateoas.config.EnableHypermediaSupport;
//import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Collections;
//
//@AutoConfiguration
//@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL}) //注入HAL渲染
//public class Config {
//    //注册Jackson2HalModule 开启Jackson JSON对HAL的支持
//    @Bean
//    public Jackson2HalModule jackson2HalModule() {
//        return new Jackson2HalModule();
//    }
//
//    @Bean
//    public RestTemplate restTemplate() {
//        final ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new Jackson2HalModule());
//
//        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
//        converter.setObjectMapper(mapper);
//
//        return new RestTemplate(Collections.<HttpMessageConverter<?>>singletonList(converter));
//    }
//}
