package com.axing.common.json.model;

import com.axing.common.json.bean.JsonProperties;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Java8TimeModule extends SimpleModule {
    public Java8TimeModule(JsonProperties jsonProperties) {
        // builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        // builder.serializerByType(Long.class, ToStringSerializer.instance);
        // builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        // 使用这个方式, 使用modules,会影响其他设置 builder.modules(new Java8TimeModule());
        // yyyy-MM-dd HH:mm:ss
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(jsonProperties.getDateFormat())));
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(jsonProperties.getDateFormat())));

        // yyyy-MM-dd
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(jsonProperties.getLocalDateFormat())));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(jsonProperties.getLocalDateFormat())));

        // HH:mm:ss
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(jsonProperties.getLocalTimeFormat())));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(jsonProperties.getLocalTimeFormat())));

    }

}
