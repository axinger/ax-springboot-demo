package com.github.axinger.config;

import cn.hutool.core.date.DatePattern;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Configuration
public class StringToObjectConverter {

    /**
     * 公共方法：处理字符串的空值和特殊值
     *
     * @param source 原始字符串
     * @return 处理后的字符串，如果为空值或特殊值则返回null
     */
    @Nullable
    private static String handleNullOrSpecial(@Nullable String source) {
        // 1. 处理null
        if (source == null) {
            return null;
        }

        // 2. 去除前后空格
        String trimmed = source.trim();

        // 3. 处理空字符串和特殊字符串
        if (trimmed.isEmpty() ||
                trimmed.equalsIgnoreCase("null") ||
                trimmed.equalsIgnoreCase("undefined") ||
                trimmed.equalsIgnoreCase("nil") ||  // 添加更多特殊值支持
                trimmed.equalsIgnoreCase("na") ||   // Not Available
                trimmed.equalsIgnoreCase("n/a")) {  // Not Applicable
            return null;
        }

        return trimmed;
    }

    @Component
    public static class StringToBooleanConverter implements Converter<String, Boolean> {
        @Override
        public Boolean convert(@Nullable String source) {
            // 使用公共方法处理空值和特殊值
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            // 转换为小写处理
            String lower = trimmed.toLowerCase();

            // 支持多种true表示
            if ("true".equals(lower) || "1".equals(lower) ||
                    "yes".equals(lower) || "on".equals(lower) ||
                    "y".equals(lower) || "t".equals(lower)) {
                return true;
            }

            // 支持多种false表示
            if ("false".equals(lower) || "0".equals(lower) ||
                    "no".equals(lower) || "off".equals(lower) ||
                    "n".equals(lower) || "f".equals(lower)) {
                return false;
            }

            // 无法识别的值返回null
            return null;
        }
    }

    @Component
    public static class StringToIntegerConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(@Nullable String source) {
            // 使用公共方法处理空值和特殊值
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            try {
                // 1. 移除可能的千位分隔符
                if (trimmed.contains(",")) {
                    trimmed = trimmed.replace(",", "");
                }

                // 2. 处理科学计数法
                if (trimmed.contains("E") || trimmed.contains("e")) {
                    double d = Double.parseDouble(trimmed);
                    return (int) d;
                }

                // 3. 解析整数
                return Integer.parseInt(trimmed);

            } catch (NumberFormatException e) {
                // 4. 处理小数情况（如果需要）
                try {
                    double d = Double.parseDouble(trimmed);
                    return (int) d; // 截断小数部分
                } catch (NumberFormatException e2) {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    // 可以继续添加其他类型的转换器，复用相同的空值处理方法
    @Component
    public static class StringToDoubleConverter implements Converter<String, Double> {
        @Override
        public Double convert(@Nullable String source) {
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            try {
                // 移除千位分隔符
                if (trimmed.contains(",")) {
                    trimmed = trimmed.replace(",", "");
                }

                return Double.parseDouble(trimmed);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    @Component
    public static class StringToLongConverter implements Converter<String, Long> {
        @Override
        public Long convert(@Nullable String source) {
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            try {
                // 移除千位分隔符
                if (trimmed.contains(",")) {
                    trimmed = trimmed.replace(",", "");
                }

                // 处理科学计数法
                if (trimmed.contains("E") || trimmed.contains("e")) {
                    double d = Double.parseDouble(trimmed);
                    return (long) d;
                }

                return Long.parseLong(trimmed);
            } catch (NumberFormatException e) {
                // 处理小数情况
                try {
                    double d = Double.parseDouble(trimmed);
                    return (long) d;
                } catch (NumberFormatException e2) {
                    return null;
                }
            }
        }
    }


    @Component
    public static class StringToLocalDateConverter implements Converter<String, LocalDate> {
        // 支持多种日期格式
        private static final DateTimeFormatter[] FORMATTERS = {
                DatePattern.NORM_DATE_FORMATTER,           // yyyy-MM-dd
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy.MM.dd"),
                DateTimeFormatter.ofPattern("yyyy年MM月dd日"),
                DateTimeFormatter.ISO_LOCAL_DATE           // yyyy-MM-dd (ISO)
        };

        @Override
        public LocalDate convert(@Nullable String source) {
            // 使用公共方法处理空值和特殊值
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            // 遍历所有支持的格式进行解析
            for (DateTimeFormatter formatter : FORMATTERS) {
                try {
                    return LocalDate.parse(trimmed, formatter);
                } catch (DateTimeParseException e) {
                    // 继续尝试下一个格式
                }
            }

            // 所有格式都无法解析，返回null
            // 如果需要更严格，可以抛出异常：
            // throw new IllegalArgumentException("无法解析日期: " + source + 
            //     "，支持的格式: yyyy-MM-dd, yyyy/MM/dd, yyyy.MM.dd");

            return null;
        }
    }


    @Component
    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        // 支持多种日期格式
        private static final DateTimeFormatter[] FORMATTERS = {
                DatePattern.NORM_DATETIME_FORMATTER,           // yyyy-MM-dd HH:mm:ss
                DatePattern.NORM_DATETIME_MINUTE_FORMATTER,           // yyyy-MM-dd HH:mm
                DatePattern.NORM_DATETIME_MS_FORMATTER,           // yyyy-MM-dd HH:mm:ss.SSS
                DatePattern.ISO8601_FORMATTER,           //yyyy-MM-dd HH:mm:ss,SSS
        };

        @Override
        public LocalDateTime convert(@Nullable String source) {
            // 使用公共方法处理空值和特殊值
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            // 遍历所有支持的格式进行解析
            for (DateTimeFormatter formatter : FORMATTERS) {
                try {
                    return LocalDateTime.parse(trimmed, formatter);
                } catch (DateTimeParseException e) {
                    // 继续尝试下一个格式
                }
            }
            return null;
        }
    }


    @Component
    public static class StringToLocalTimeConverter implements Converter<String, LocalTime> {
        // 支持多种日期格式
        private static final DateTimeFormatter[] FORMATTERS = {
                DatePattern.NORM_TIME_FORMATTER,           // HH:mm:ss
        };

        @Override
        public LocalTime convert(@Nullable String source) {
            // 使用公共方法处理空值和特殊值
            String trimmed = handleNullOrSpecial(source);
            if (Objects.isNull(trimmed)) {
                return null;
            }

            // 遍历所有支持的格式进行解析
            for (DateTimeFormatter formatter : FORMATTERS) {
                try {
                    return LocalTime.parse(trimmed, formatter);
                } catch (DateTimeParseException e) {
                    // 继续尝试下一个格式
                }
            }
            return null;
        }
    }

}