//package com.github.axinger;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.TimeZone;
//
//@Configuration
//public class JacksonConfig {
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        // 注册JavaTimeModule并自定义序列化格式
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        javaTimeModule.addSerializer(
//                LocalDateTime.class,
//                new LocalDateTimeSerializer(formatter)
//        );
//        javaTimeModule.addDeserializer(
//                LocalDateTime.class,
//                new LocalDateTimeDeserializer(formatter)
//        );
//        objectMapper.registerModule(javaTimeModule);
//        // 禁用时间戳格式
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        // 设置时区（可选）
//        objectMapper.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
//        return objectMapper;
//    }
//}
