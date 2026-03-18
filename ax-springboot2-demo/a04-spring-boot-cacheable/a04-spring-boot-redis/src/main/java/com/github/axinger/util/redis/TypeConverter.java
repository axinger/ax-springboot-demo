package com.github.axinger.util.redis;

/**
 * 类型转换器接口
 * <p>
 * 实现此接口可以自定义 Redis 字符串值到 Java 对象的转换逻辑
 *
 * @param <T> 目标类型
 * @author xing
 */
public interface TypeConverter<T> {

    /**
     * 将字符串值转换为目标类型
     *
     * @param value 字符串值
     * @return 转换后的对象
     * @throws Exception 转换失败时抛出异常
     */
    T convert(String value) throws Exception;

    /**
     * 返回此转换器支持的目标类型
     *
     * @return 目标类型的 Class 对象
     */
    Class<T> supportType();

    /**
     * 转换器优先级（数字越小优先级越高）
     * <p>
     * 默认优先级为 100，内置转换器优先级为 0-99
     * 自定义转换器建议使用 100+
     *
     * @return 优先级
     */
    default int priority() {
        return 100;
    }
}