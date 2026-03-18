package com.github.axinger.util.redis;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型转换器注册中心
 * <p>
 * 管理所有 Redis 值类型转换器，支持动态注册和查找
 * 线程安全
 *
 * @author xing
 */
@Slf4j
public class TypeConverterRegistry {

    /**
     * 转换器缓存：类型 -> 转换器
     */
    private final Map<Class<?>, TypeConverter<?>> converterCache = new ConcurrentHashMap<>();

    /**
     * 所有已注册的转换器列表（用于支持优先级）
     */
    private final List<TypeConverter<?>> converters = new ArrayList<>();

    /**
     * 单例实例
     */
    private static final TypeConverterRegistry INSTANCE = new TypeConverterRegistry();

    private TypeConverterRegistry() {
        // 注册内置转换器
        registerBuiltInConverters();
    }

    /**
     * 获取单例实例
     *
     * @return 注册中心实例
     */
    public static TypeConverterRegistry getInstance() {
        return INSTANCE;
    }

    /**
     * 注册内置转换器
     */
    private void registerBuiltInConverters() {
        // 基本类型
        register(new BuiltInConverters.BooleanConverter());
        register(new BuiltInConverters.IntegerConverter());
        register(new BuiltInConverters.LongConverter());
        register(new BuiltInConverters.DoubleConverter());
        register(new BuiltInConverters.FloatConverter());
        register(new BuiltInConverters.ByteConverter());
        register(new BuiltInConverters.ShortConverter());

        // 大数类型
        register(new BuiltInConverters.BigDecimalConverter());
        register(new BuiltInConverters.BigIntegerConverter());

        // 日期时间类型
        register(new BuiltInConverters.LocalDateTimeConverter());
        register(new BuiltInConverters.LocalDateConverter());
        register(new BuiltInConverters.LocalTimeConverter());
        register(new BuiltInConverters.DateConverter());
        register(new BuiltInConverters.InstantConverter());

        log.info("TypeConverterRegistry initialized with {} built-in converters", converters.size());
    }

    /**
     * 注册类型转换器
     *
     * @param converter 转换器实例
     * @param <T>       目标类型
     */
    public <T> void register(TypeConverter<T> converter) {
        if (converter == null) {
            throw new IllegalArgumentException("Converter cannot be null");
        }

        Class<T> type = converter.supportType();
        if (type == null) {
            throw new IllegalArgumentException("Converter must support a type");
        }

        synchronized (this) {
            // 添加到列表
            converters.add(converter);
            // 按优先级排序（优先级小的在前）
            converters.sort((c1, c2) -> Integer.compare(c1.priority(), c2.priority()));

            // 更新缓存
            converterCache.put(type, converter);

            log.debug("Registered type converter: {} for type: {}, priority: {}",
                    converter.getClass().getSimpleName(), type.getSimpleName(), converter.priority());
        }
    }

    /**
     * 注册多个转换器
     *
     * @param converters 转换器数组
     */
    public void registerAll(TypeConverter<?>... converters) {
        for (TypeConverter<?> converter : converters) {
            register(converter);
        }
    }

    /**
     * 获取指定类型的转换器
     *
     * @param type 目标类型
     * @param <T>  目标类型
     * @return 转换器，如果不存在则返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> TypeConverter<T> getConverter(Class<T> type) {
        if (type == null) {
            return null;
        }

        // 从缓存中获取
        TypeConverter<?> converter = converterCache.get(type);
        if (converter != null) {
            return (TypeConverter<T>) converter;
        }

        // 处理基本类型（int, long, boolean 等）
        Class<?> wrapperType = getWrapperType(type);
        if (wrapperType != type) {
            converter = converterCache.get(wrapperType);
            if (converter != null) {
                // 缓存基本类型映射
                converterCache.put(type, converter);
                return (TypeConverter<T>) converter;
            }
        }

        return null;
    }

    /**
     * 判断是否支持指定类型的转换
     *
     * @param type 目标类型
     * @return 是否支持
     */
    public boolean supports(Class<?> type) {
        return getConverter(type) != null;
    }

    /**
     * 移除指定类型的转换器
     *
     * @param type 目标类型
     */
    public void unregister(Class<?> type) {
        synchronized (this) {
            TypeConverter<?> removed = converterCache.remove(type);
            if (removed != null) {
                converters.remove(removed);
                log.debug("Unregistered type converter for type: {}", type.getSimpleName());
            }
        }
    }

    /**
     * 清空所有转换器（包括内置转换器）
     */
    public void clear() {
        synchronized (this) {
            converterCache.clear();
            converters.clear();
            log.info("Cleared all type converters");
        }
    }

    /**
     * 重置为默认状态（重新注册内置转换器）
     */
    public void reset() {
        clear();
        registerBuiltInConverters();
    }

    /**
     * 获取基本类型对应的包装类型
     *
     * @param type 原始类型
     * @return 包装类型
     */
    private Class<?> getWrapperType(Class<?> type) {
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        if (type == char.class) return Character.class;
        return type;
    }

    /**
     * 获取所有已注册的转换器数量
     *
     * @return 转换器数量
     */
    public int size() {
        return converters.size();
    }

    /**
     * 获取所有支持的类型
     *
     * @return 类型列表
     */
    public List<Class<?>> getSupportedTypes() {
        return new ArrayList<>(converterCache.keySet());
    }
}