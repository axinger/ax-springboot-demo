package com.github.axinger.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Configuration
//@EnableConfigurationProperties(JsonProperties.class)
//@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class ObjectMapperConfig {

    private final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    private final String dateFormat = "yyyy-MM-dd";
    private final String timeFormat = "HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.simpleDateFormat(dateTimeFormat);
            // 默认情况下如果遇到目标类中不存在的属性就会抛出异常。
            // builder.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, properties.isFailOnUnknownProperties());
            // Spring Boot 默认就是 FAIL_ON_UNKNOWN_PROPERTIES = false，即忽略未知属性
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            // 使用这个方式, 使用modules,会影响其他设置 builder.modules(new Java8TimeModule());
            // builder.modules(new Java8TimeModule(properties));
            // yyyy-MM-dd HH:mm:ss
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));

            // yyyy-MM-dd
            builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializerByType(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));

            // HH:mm:ss
            builder.serializerByType(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(timeFormat)));
            builder.deserializerByType(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(timeFormat)));
        };
    }

}
