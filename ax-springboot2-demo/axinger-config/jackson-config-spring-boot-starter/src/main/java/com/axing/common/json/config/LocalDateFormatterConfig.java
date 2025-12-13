package com.axing.common.json.config;

import cn.hutool.core.date.DatePattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

/// 格式化get请求java.time.参数
@Slf4j
@Configuration
public class LocalDateFormatterConfig {
    @Bean
    public Formatter<LocalTime> localTimeFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public String print(@NonNull LocalTime object, @NonNull Locale locale) {
                return object.format(DatePattern.NORM_TIME_FORMATTER);
            }

            @Override
            @NonNull
            public LocalTime parse(@NonNull String text, @NonNull Locale locale) {
                return LocalTime.parse(text, DatePattern.NORM_TIME_FORMATTER);
            }
        };
    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public String print(@NonNull LocalDate object, @NonNull Locale locale) {
                return object.format(DatePattern.NORM_DATE_FORMATTER);
            }

            @Override
            @NonNull
            public LocalDate parse(@NonNull String text, @NonNull Locale locale) {
                return LocalDate.parse(text, DatePattern.NORM_DATE_FORMATTER);
            }
        };
    }

    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<>() {
            @Override
            @NonNull
            public String print(@NonNull LocalDateTime object, @NonNull Locale locale) {
                return object.format(DatePattern.NORM_DATETIME_FORMATTER);
            }

            @Override
            @NonNull
            public LocalDateTime parse(@NonNull String text, @NonNull Locale locale) {
                return LocalDateTime.parse(text, DatePattern.NORM_DATETIME_FORMATTER);
            }
        };
    }
}
