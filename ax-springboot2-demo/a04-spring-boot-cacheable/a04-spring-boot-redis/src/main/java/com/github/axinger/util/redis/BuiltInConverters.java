package com.github.axinger.util.redis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 内置类型转换器集合
 *
 * @author xing
 */
public class BuiltInConverters {

    /**
     * Boolean 转换器
     */
    public static class BooleanConverter implements TypeConverter<Boolean> {
        @Override
        public Boolean convert(String value) {
            return Boolean.valueOf(value);
        }

        @Override
        public Class<Boolean> supportType() {
            return Boolean.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * Integer 转换器
     */
    public static class IntegerConverter implements TypeConverter<Integer> {
        @Override
        public Integer convert(String value) {
            return Integer.valueOf(value);
        }

        @Override
        public Class<Integer> supportType() {
            return Integer.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * Long 转换器
     */
    public static class LongConverter implements TypeConverter<Long> {
        @Override
        public Long convert(String value) {
            return Long.valueOf(value);
        }

        @Override
        public Class<Long> supportType() {
            return Long.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * Double 转换器
     */
    public static class DoubleConverter implements TypeConverter<Double> {
        @Override
        public Double convert(String value) {
            return Double.valueOf(value);
        }

        @Override
        public Class<Double> supportType() {
            return Double.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * Float 转换器
     */
    public static class FloatConverter implements TypeConverter<Float> {
        @Override
        public Float convert(String value) {
            return Float.valueOf(value);
        }

        @Override
        public Class<Float> supportType() {
            return Float.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * Byte 转换器
     */
    public static class ByteConverter implements TypeConverter<Byte> {
        @Override
        public Byte convert(String value) {
            return Byte.valueOf(value);
        }

        @Override
        public Class<Byte> supportType() {
            return Byte.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * Short 转换器
     */
    public static class ShortConverter implements TypeConverter<Short> {
        @Override
        public Short convert(String value) {
            return Short.valueOf(value);
        }

        @Override
        public Class<Short> supportType() {
            return Short.class;
        }

        @Override
        public int priority() {
            return 10;
        }
    }

    /**
     * BigDecimal 转换器
     */
    public static class BigDecimalConverter implements TypeConverter<BigDecimal> {
        @Override
        public BigDecimal convert(String value) {
            return new BigDecimal(value);
        }

        @Override
        public Class<BigDecimal> supportType() {
            return BigDecimal.class;
        }

        @Override
        public int priority() {
            return 20;
        }
    }

    /**
     * BigInteger 转换器
     */
    public static class BigIntegerConverter implements TypeConverter<BigInteger> {
        @Override
        public BigInteger convert(String value) {
            return new BigInteger(value);
        }

        @Override
        public Class<BigInteger> supportType() {
            return BigInteger.class;
        }

        @Override
        public int priority() {
            return 20;
        }
    }

    /**
     * LocalDateTime 转换器
     * <p>
     * 支持 ISO-8601 格式：2024-01-01T10:30:00
     */
    public static class LocalDateTimeConverter implements TypeConverter<LocalDateTime> {
        @Override
        public LocalDateTime convert(String value) {
            return LocalDateTime.parse(value);
        }

        @Override
        public Class<LocalDateTime> supportType() {
            return LocalDateTime.class;
        }

        @Override
        public int priority() {
            return 30;
        }
    }

    /**
     * LocalDate 转换器
     * <p>
     * 支持 ISO-8601 格式：2024-01-01
     */
    public static class LocalDateConverter implements TypeConverter<LocalDate> {
        @Override
        public LocalDate convert(String value) {
            return LocalDate.parse(value);
        }

        @Override
        public Class<LocalDate> supportType() {
            return LocalDate.class;
        }

        @Override
        public int priority() {
            return 30;
        }
    }

    /**
     * LocalTime 转换器
     * <p>
     * 支持 ISO-8601 格式：10:30:00
     */
    public static class LocalTimeConverter implements TypeConverter<LocalTime> {
        @Override
        public LocalTime convert(String value) {
            return LocalTime.parse(value);
        }

        @Override
        public Class<LocalTime> supportType() {
            return LocalTime.class;
        }

        @Override
        public int priority() {
            return 30;
        }
    }

    /**
     * Date 转换器
     * <p>
     * 支持以下格式：
     * 1. 时间戳（毫秒）：1234567890123
     * 2. ISO-8601 格式：2024-01-01T10:30:00
     */
    public static class DateConverter implements TypeConverter<Date> {
        @Override
        public Date convert(String value) throws Exception {
            // 尝试解析为时间戳
            try {
                long timestamp = Long.parseLong(value);
                return new Date(timestamp);
            } catch (NumberFormatException e) {
                // 尝试解析为 ISO-8601 格式
                LocalDateTime localDateTime = LocalDateTime.parse(value);
                return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            }
        }

        @Override
        public Class<Date> supportType() {
            return Date.class;
        }

        @Override
        public int priority() {
            return 40;
        }
    }

    /**
     * Instant 转换器
     * <p>
     * 支持 ISO-8601 格式：2024-01-01T10:30:00Z
     */
    public static class InstantConverter implements TypeConverter<Instant> {
        @Override
        public Instant convert(String value) throws Exception {
            // 尝试解析为时间戳
            try {
                long timestamp = Long.parseLong(value);
                return Instant.ofEpochMilli(timestamp);
            } catch (NumberFormatException e) {
                // 尝试解析为 ISO-8601 格式
                return Instant.parse(value);
            }
        }

        @Override
        public Class<Instant> supportType() {
            return Instant.class;
        }

        @Override
        public int priority() {
            return 40;
        }
    }
}