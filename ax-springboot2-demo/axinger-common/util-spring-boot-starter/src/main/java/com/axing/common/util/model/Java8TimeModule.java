package com.axing.common.util.model;

import com.axing.common.util.utils.DateTimeFormatterUtil;
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

    public Java8TimeModule() {
        // builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
        // builder.serializerByType(Long.class, ToStringSerializer.instance);
        // builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        // 使用这个方式, 使用modules,会影响其他设置 builder.modules(new Java8TimeModule());
        // yyyy-MM-dd HH:mm:ss
        this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.dateFormat)));
        this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.dateFormat)));

        // yyyy-MM-dd
        this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.localDateFormat)));
        this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.localDateFormat)));

        // HH:mm:ss
        this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.localTimeFormat)));
        this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateTimeFormatterUtil.localTimeFormat)));
    }

}
