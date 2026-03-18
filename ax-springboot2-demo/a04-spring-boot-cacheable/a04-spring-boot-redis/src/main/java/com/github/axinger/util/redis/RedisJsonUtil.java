package com.github.axinger.util.redis;

import cn.hutool.core.collection.ListUtil;
import com.axing.common.json.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis JSON工具类
 * <p>
 * 存储策略：
 * - 字符串类型：直接存储原始字符串
 * - 对象类型：序列化为JSON字符串后存储
 * <p>
 * 优势：
 * - 数据可读性好（JSON格式）
 * - 跨语言支持（其他系统可以直接读取JSON）
 * - 存储空间相对较小（相比Java序列化）
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisJsonUtil {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 类型转换器注册中心
     */
    private final TypeConverterRegistry converterRegistry = TypeConverterRegistry.getInstance();

    /**
     * 将字符串值转换为指定类型
     * <p>
     * 转换优先级：
     * 1. String 类型 - 直接返回
     * 2. 注册的类型转换器 - 使用转换器转换
     * 3. JSON 反序列化 - 复杂对象使用 JSON 反序列化
     *
     * @param value 字符串值
     * @param type  目标类型
     * @return 转换后的对象
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValue(String value, Class<T> type) {
        if (value == null) {
            return null;
        }

        // 字符串类型直接返回
        if (type == String.class) {
            return type.cast(value);
        }

        // 使用注册的类型转换器
        TypeConverter<T> converter = converterRegistry.getConverter(type);
        if (converter != null) {
            try {
                return converter.convert(value);
            } catch (Exception e) {
                log.debug("Type converter failed for type: {}, trying JSON deserialization", type.getName());
                // 转换器失败，继续尝试 JSON 反序列化
            }
        }

        // 使用 JSON 反序列化（处理复杂对象）
        return JsonUtil.toBean(value, type);
    }

    /**
     * 注册自定义类型转换器
     * <p>
     * 允许用户注册自己的类型转换逻辑
     *
     * @param converter 转换器实例
     * @param <T>       目标类型
     */
    public <T> void registerConverter(TypeConverter<T> converter) {
        converterRegistry.register(converter);
        log.info("Registered custom type converter: {} for type: {}",
                converter.getClass().getSimpleName(),
                converter.supportType().getSimpleName());
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return the boolean
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil expire error :", e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("RedisJsonUtil hasKey error :", e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public boolean del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                return stringRedisTemplate.delete(key[0]);
            } else {
                List<String> list = ListUtil.toList(key);
                return stringRedisTemplate.delete(list) > 0;
            }
        }
        return false;
    }

    /**
     * 批量删除
     *
     * @param keys keys
     */
    public Long delKeys(Set<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    /**
     * 模糊匹配key值 返回key set
     *
     * @param pattern 匹配模式
     * @return Set<String>
     */
    public Set<String> keys(String pattern) {
        return pattern == null ? null : stringRedisTemplate.keys(pattern);
    }

    //============================String=============================

    /**
     * 获取字符串值
     *
     * @param key 键
     * @return 字符串值
     */
    public String getString(String key) {
        return key == null ? null : stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取对象值（自动JSON反序列化）
     * <p>
     * 支持基本类型的直接转换：String, Boolean, Integer, Long, Double, Float
     * 复杂对象使用JSON反序列化
     *
     * @param key  键
     * @param type 目标类型
     * @return 对象
     */
    public <T> T get(String key, Class<T> type) {
        if (key == null) {
            return null;
        }
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        try {
            return convertValue(value, type);
        } catch (Exception e) {
            log.error("RedisJsonUtil get error, key={}, type={}, value={}", key, type.getName(), value, e);
            return null;
        }
    }

    /**
     * 获取List对象（自动JSON反序列化）
     *
     * @param key         键
     * @param elementType List元素类型
     * @return List对象
     */
    public <T> List<T> getList(String key, Class<T> elementType) {
        if (key == null) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        try {

            return JsonUtil.toBeanList(json, elementType);
        } catch (Exception e) {
            log.error("RedisJsonUtil getList error, key={}, elementType={}", key, elementType.getName(), e);
            return null;
        }
    }

    /**
     * 获取复杂泛型对象（使用TypeReference）
     * <p>
     * 使用示例：
     * Map<String, List<User>> map = getObject(key, new TypeReference<Map<String, List<User>>>(){});
     *
     * @param key           键
     * @param typeReference 类型引用
     * @return 对象
     */
    public <T> T getObject(String key, TypeReference<T> typeReference) {
        if (key == null) {
            return null;
        }
        String json = stringRedisTemplate.opsForValue().get(key);
        if (json == null) {
            return null;
        }
        try {
            return JsonUtil.toBean(json, typeReference);
        } catch (Exception e) {
            log.error("RedisJsonUtil getObject error, key={}", key, e);
            return null;
        }
    }

    /**
     * 存储字符串
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setString(String key, String value) {
        try {
            stringRedisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil setString error :", e);
            return false;
        }
    }

    /**
     * 存储对象（自动JSON序列化）
     *
     * @param key   键
     * @param value 值（如果是String直接存储，否则序列化为JSON）
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            if (value instanceof String) {
                stringRedisTemplate.opsForValue().set(key, (String) value);
            } else {
                String json = JsonUtil.toJson(value);
                stringRedisTemplate.opsForValue().set(key, json);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil set error :", e);
            return false;
        }
    }

    /**
     * 存储对象并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true成功 false失败
     */
    public boolean set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            if (value instanceof String) {
                stringRedisTemplate.opsForValue().set(key, (String) value, timeout, unit);
            } else {
                String json = JsonUtil.toJson(value);
                stringRedisTemplate.opsForValue().set(key, json, timeout, unit);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil set error :", e);
            return false;
        }
    }

    /**
     * 存储对象并设置过期时间（秒）
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                return set(key, value, time, TimeUnit.SECONDS);
            } else {
                return set(key, value);
            }
        } catch (Exception e) {
            log.error("RedisJsonUtil set error :", e);
            return false;
        }
    }

    /**
     * 判断Key是否存在并缓存
     *
     * @param key   key
     * @param value value
     * @param time  time(毫秒)
     * @return Boolean
     */
    public Boolean setNX(String key, String value, long time) {
        try {
            return stringRedisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("RedisJsonUtil setNX error :", e);
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return the long
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(大于0)
     * @return the long
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Hash=================================

    /**
     * HGET - 获取字符串值
     *
     * @param key   键 不能为null
     * @param field 字段 不能为null
     * @return 字符串值
     */
    public String hGetString(String key, String field) {
        Object value = stringRedisTemplate.opsForHash().get(key, field);
        return value == null ? null : value.toString();
    }

    /**
     * HGET - 获取对象（自动JSON反序列化）
     * <p>
     * 支持基本类型的直接转换：String, Boolean, Integer, Long, Double, Float
     * 复杂对象使用JSON反序列化
     *
     * @param key   键 不能为null
     * @param field 字段 不能为null
     * @param type  目标类型
     * @return 对象
     */
    public <T> T hGet(String key, String field, Class<T> type) {
        Object obj = stringRedisTemplate.opsForHash().get(key, field);
        if (obj == null) {
            return null;
        }
        String value = obj.toString();

        try {
            return convertValue(value, type);
        } catch (Exception e) {
            log.error("RedisJsonUtil hGet error, key={}, field={}, type={}, value={}", key, field, type.getName(), value, e);
            return null;
        }
    }

    /**
     * HGET - 获取List对象（自动JSON反序列化）
     *
     * @param key         键
     * @param field       字段
     * @param elementType List元素类型
     * @return List对象
     */
    public <T> List<T> hGetList(String key, String field, Class<T> elementType) {
        Object value = stringRedisTemplate.opsForHash().get(key, field);
        if (value == null) {
            return null;
        }
        String json = value.toString();
        try {
            return JsonUtil.toBeanList(json, elementType);
        } catch (Exception e) {
            log.error("RedisJsonUtil hGetList error, key={}, field={}, elementType={}", key, field, elementType.getName(), e);
            return null;
        }
    }

    /**
     * HGET - 获取复杂泛型对象（使用TypeReference）
     *
     * @param key           键
     * @param field         字段
     * @param typeReference 类型引用
     * @return 对象
     */
    public <T> T hGetObject(String key, String field, TypeReference<T> typeReference) {
        Object value = stringRedisTemplate.opsForHash().get(key, field);
        if (value == null) {
            return null;
        }
        String json = value.toString();
        try {
            return JsonUtil.toBean(json, typeReference);
        } catch (Exception e) {
            log.error("RedisJsonUtil hGetObject error, key={}, field={}", key, field, e);
            return null;
        }
    }

    /**
     * HGETALL - 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值 map
     */
    public Map<Object, Object> hGetAll(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * HGETALL - 获取hashKey对应的所有键值（自动JSON反序列化）
     *
     * @param key         键
     * @param elementType 元素类型
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hGetAll(String key, Class<T> elementType) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String json = entry.getValue().toString();
            try {
                T value = elementType == String.class ? elementType.cast(json) : JsonUtil.toBean(json, elementType);
                result.put(entry.getKey().toString(), value);
            } catch (Exception e) {
                log.error("RedisJsonUtil hGetAll parse error, key={}, field={}, elementType={}",
                        key, entry.getKey(), elementType.getName(), e);
            }
        }
        return result;
    }

    /**
     * HSET - 向hash表中放入数据（自动JSON序列化）
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hSet(String key, String field, Object value) {
        try {
            if (value instanceof String) {
                stringRedisTemplate.opsForHash().put(key, field, value);
            } else {
                String json = JsonUtil.toJson(value);
                stringRedisTemplate.opsForHash().put(key, field, json);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil hSet error :", e);
            return false;
        }
    }

    /**
     * HMSET - 批量存储（自动JSON序列化）
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hSetAll(String key, Map<String, Object> map) {
        try {
            Map<String, String> jsonMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String) {
                    jsonMap.put(entry.getKey(), (String) value);
                } else {
                    jsonMap.put(entry.getKey(), JsonUtil.toJson(value));
                }
            }
            stringRedisTemplate.opsForHash().putAll(key, jsonMap);
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil hSetAll error :", e);
            return false;
        }
    }

    /**
     * HMSET - 批量存储并设置过期时间
     *
     * @param key     键
     * @param map     对应多个键值
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return true 成功 false 失败
     */
    public boolean hSetAll(String key, Map<String, Object> map, long timeout, TimeUnit unit) {
        try {
            hSetAll(key, map);
            if (timeout > 0) {
                stringRedisTemplate.expire(key, timeout, unit);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil hSetAll error :", e);
            return false;
        }
    }

    /**
     * HDEL - 删除hash表中的字段
     *
     * @param key    键 不能为null
     * @param fields 字段 可以是多个 不能为null
     */
    public Long hDel(String key, Object... fields) {
        return stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * DEL - 删除整个hash表
     *
     * @param key 键
     */
    public boolean hDelAll(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * HEXISTS - 判断hash表中是否存在该字段
     *
     * @param key   键 不能为null
     * @param field 字段 不能为null
     * @return true 存在 false不存在
     */
    public boolean hExists(String key, String field) {
        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * HINCRBY - hash字段递增（如果不存在会创建）
     *
     * @param key   键
     * @param field 字段
     * @param delta 增量
     * @return 递增后的值
     */
    public double hIncrBy(String key, String field, double delta) {
        return stringRedisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * HINCRBY - hash字段递减
     *
     * @param key   键
     * @param field 字段
     * @param delta 减量
     * @return 递减后的值
     */
    public double hDecrBy(String key, String field, double delta) {
        return stringRedisTemplate.opsForHash().increment(key, field, -delta);
    }

    //================================Set=================================

    /**
     * SISMEMBER - 判断value是否是set成员
     *
     * @param key    键
     * @param member 成员值
     * @return true 存在 false不存在
     */
    public Boolean sIsMember(String key, String member) {
        try {
            return stringRedisTemplate.opsForSet().isMember(key, member);
        } catch (Exception e) {
            log.error("RedisJsonUtil sIsMember error :", e);
            return false;
        }
    }

    /**
     * SADD - 将数据放入set缓存
     *
     * @param key     键
     * @param members 成员值 可以是多个
     * @return 成功个数
     */
    public Long sAdd(String key, String... members) {
        try {
            return stringRedisTemplate.opsForSet().add(key, members);
        } catch (Exception e) {
            log.error("RedisJsonUtil sAdd error :", e);
            return 0L;
        }
    }

    /**
     * SADD - 将数据放入set缓存并设置过期时间
     *
     * @param key     键
     * @param timeout 过期时间(秒)
     * @param members 成员值 可以是多个
     * @return 成功个数
     */
    public Long sAdd(String key, long timeout, String... members) {
        try {
            Long count = stringRedisTemplate.opsForSet().add(key, members);
            if (timeout > 0) {
                expire(key, timeout);
            }
            return count;
        } catch (Exception e) {
            log.error("RedisJsonUtil sAdd error :", e);
            return 0L;
        }
    }

    /**
     * SCARD - 获取set的大小
     *
     * @param key 键
     * @return set大小
     */
    public Long sSize(String key) {
        try {
            return stringRedisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("RedisJsonUtil sSize error :", e);
            return 0L;
        }
    }

    /**
     * SREM - 移除set中的成员
     *
     * @param key     键
     * @param members 成员值 可以是多个
     * @return 移除的个数
     */
    public Long sRem(String key, Object... members) {
        try {
            return stringRedisTemplate.opsForSet().remove(key, members);
        } catch (Exception e) {
            log.error("RedisJsonUtil sRem error :", e);
            return 0L;
        }
    }

    //===============================List=================================

    /**
     * LLEN - 获取list的长度
     *
     * @param key 键
     * @return list长度
     */
    public Long lSize(String key) {
        try {
            return stringRedisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("RedisJsonUtil lSize error :", e);
            return 0L;
        }
    }

    /**
     * LINDEX - 通过索引获取list中的字符串值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 字符串值
     */
    public String lIndex(String key, long index) {
        try {
            return stringRedisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("RedisJsonUtil lIndex error :", e);
            return null;
        }
    }

    /**
     * LINDEX - 通过索引获取list中的对象（自动JSON反序列化）
     * <p>
     * 支持基本类型的直接转换：String, Boolean, Integer, Long, Double, Float
     * 复杂对象使用JSON反序列化
     *
     * @param key   键
     * @param index 索引
     * @param type  目标类型
     * @return 对象
     */
    public <T> T lIndex(String key, long index, Class<T> type) {
        try {
            String value = stringRedisTemplate.opsForList().index(key, index);
            return convertValue(value, type);
        } catch (Exception e) {
            log.error("RedisJsonUtil lIndex error, key={}, index={}, type={}", key, index, type.getName(), e);
            return null;
        }
    }

    /**
     * RPUSH - 从右边将值放入list（自动JSON序列化）
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean rPush(String key, Object value) {
        try {
            if (value instanceof String) {
                stringRedisTemplate.opsForList().rightPush(key, (String) value);
            } else {
                stringRedisTemplate.opsForList().rightPush(key, JsonUtil.toJson(value));
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil rPush error :", e);
            return false;
        }
    }

    /**
     * RPUSH - 从右边将值放入list并设置过期时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return true成功 false失败
     */
    public boolean rPush(String key, Object value, long time) {
        try {
            rPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil rPush error :", e);
            return false;
        }
    }

    /**
     * RPUSH - 从右边批量将list放入缓存（自动JSON序列化）
     *
     * @param key  键
     * @param list 值列表
     * @return true成功 false失败
     */
    public boolean rPushAll(String key, List<?> list) {
        try {
            String[] jsonArray = list.stream()
                    .map(item -> item instanceof String ? (String) item : JsonUtil.toJson(item))
                    .toArray(String[]::new);
            stringRedisTemplate.opsForList().rightPushAll(key, jsonArray);
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil rPushAll error :", e);
            return false;
        }
    }

    /**
     * RPUSH - 从右边批量将list放入缓存并设置过期时间
     *
     * @param key  键
     * @param list 值列表
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean rPushAll(String key, List<?> list, long time) {
        try {
            rPushAll(key, list);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil rPushAll error :", e);
            return false;
        }
    }

    /**
     * LSET - 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true成功 false失败
     */
    public boolean lSet(String key, long index, Object value) {
        try {
            if (value instanceof String) {
                stringRedisTemplate.opsForList().set(key, index, (String) value);
            } else {
                stringRedisTemplate.opsForList().set(key, index, JsonUtil.toJson(value));
            }
            return true;
        } catch (Exception e) {
            log.error("RedisJsonUtil lSet error :", e);
            return false;
        }
    }

    /**
     * LREM - 移除N个值为value的元素
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public Long lRem(String key, long count, String value) {
        try {
            return stringRedisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error("RedisJsonUtil lRem error :", e);
            return 0L;
        }
    }

    /**
     * LPOP - 从左边弹出数据（返回字符串）
     *
     * @param key 键
     * @return 弹出的值（字符串）
     */
    public String lPop(String key) {
        try {
            return stringRedisTemplate.opsForList().leftPop(key);
        } catch (Exception e) {
            log.error("RedisJsonUtil lPop error :", e);
            return null;
        }
    }

    /**
     * LPOP - 从左边弹出数据（自动JSON反序列化）
     * <p>
     * 支持基本类型的直接转换：String, Boolean, Integer, Long, Double, Float
     * 复杂对象使用JSON反序列化
     *
     * @param key  键
     * @param type 目标类型
     * @return 弹出的对象
     */
    public <T> T lPop(String key, Class<T> type) {
        try {
            String value = stringRedisTemplate.opsForList().leftPop(key);
            return convertValue(value, type);
        } catch (Exception e) {
            log.error("RedisJsonUtil lPop error, key={}, type={}", key, type.getName(), e);
            return null;
        }
    }

    /**
     * RPOP - 从右边弹出数据（返回字符串）
     *
     * @param key 键
     * @return 弹出的值（字符串）
     */
    public String rPop(String key) {
        try {
            return stringRedisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("RedisJsonUtil rPop error :", e);
            return null;
        }
    }

    /**
     * RPOP - 从右边弹出数据（自动JSON反序列化）
     * <p>
     * 支持基本类型的直接转换：String, Boolean, Integer, Long, Double, Float
     * 复杂对象使用JSON反序列化
     *
     * @param key  键
     * @param type 目标类型
     * @return 弹出的对象
     */
    public <T> T rPop(String key, Class<T> type) {
        try {
            String value = stringRedisTemplate.opsForList().rightPop(key);
            return convertValue(value, type);
        } catch (Exception e) {
            log.error("RedisJsonUtil rPop error, key={}, type={}", key, type.getName(), e);
            return null;
        }
    }

    /**
     * 模糊删除（删除匹配pattern的所有key）
     *
     * @param pattern 匹配模式
     * @return 删除的数量
     */
    public Long delPattern(String pattern) {
        try {
            Set<String> keys = stringRedisTemplate.keys(pattern);
            if (!keys.isEmpty()) {
                return stringRedisTemplate.delete(keys);
            }
            return 0L;
        } catch (Exception e) {
            log.error("RedisJsonUtil delPattern error :", e);
            return 0L;
        }
    }

    //===============================向后兼容方法（已废弃）=================================

    /**
     * @deprecated 请使用 {@link #hGetString(String, String)}
     */
    @Deprecated
    public String getMapString(String key, String item) {
        return hGetString(key, item);
    }

    /**
     * @deprecated 请使用 {@link #hGet(String, String, Class)}
     */
    @Deprecated
    public <T> T getMap(String key, String item, Class<T> type) {
        return hGet(key, item, type);
    }

    /**
     * @deprecated 请使用 {@link #hGetList(String, String, Class)}
     */
    @Deprecated
    public <T> List<T> getMapList(String key, String item, Class<T> elementType) {
        return hGetList(key, item, elementType);
    }

    /**
     * @deprecated 请使用 {@link #hGetObject(String, String, TypeReference)}
     */
    @Deprecated
    public <T> T getMapObject(String key, String item, TypeReference<T> typeReference) {
        return hGetObject(key, item, typeReference);
    }

    /**
     * @deprecated 请使用 {@link #hGetAll(String)}
     */
    @Deprecated
    public Map<Object, Object> getMapAll(String key) {
        return hGetAll(key);
    }

    /**
     * @deprecated 请使用 {@link #hGetAll(String, Class)}
     */
    @Deprecated
    public <T> Map<String, T> getMapAll(String key, Class<T> elementType) {
        return hGetAll(key, elementType);
    }

    /**
     * @deprecated 请使用 {@link #hSet(String, String, Object)}
     */
    @Deprecated
    public boolean putMap(String key, String item, Object value) {
        return hSet(key, item, value);
    }

    /**
     * @deprecated 请使用 {@link #hSetAll(String, Map)}
     */
    @Deprecated
    public boolean putMapAll(String key, Map<String, Object> map) {
        return hSetAll(key, map);
    }

    /**
     * @deprecated 请使用 {@link #hSetAll(String, Map, long, TimeUnit)}
     */
    @Deprecated
    public boolean putMapAll(String key, Map<String, Object> map, long timeout, TimeUnit unit) {
        return hSetAll(key, map, timeout, unit);
    }

    /**
     * @deprecated 请使用 {@link #hDel(String, Object...)}
     */
    @Deprecated
    public Long deleteMap(String key, Object... item) {
        return hDel(key, item);
    }

    /**
     * @deprecated 请使用 {@link #hDelAll(String)}
     */
    @Deprecated
    public boolean deleteMapAll(String key) {
        return hDelAll(key);
    }

    /**
     * @deprecated 请使用 {@link #hExists(String, String)}
     */
    @Deprecated
    public boolean hasMapKey(String key, String item) {
        return hExists(key, item);
    }

    /**
     * @deprecated 请使用 {@link #sIsMember(String, String)}
     */
    @Deprecated
    public Boolean sHasKey(String key, String value) {
        return sIsMember(key, value);
    }

    /**
     * @deprecated 请使用 {@link #sAdd(String, long, String...)}
     */
    @Deprecated
    public Long sAddAndTime(String key, long time, String... values) {
        return sAdd(key, time, values);
    }

    /**
     * @deprecated 请使用 {@link #sSize(String)}
     */
    @Deprecated
    public Long sGetSetSize(String key) {
        return sSize(key);
    }

    /**
     * @deprecated 请使用 {@link #sRem(String, Object...)}
     */
    @Deprecated
    public Long sRemove(String key, Object... values) {
        return sRem(key, values);
    }

    /**
     * @deprecated 请使用 {@link #lSize(String)}
     */
    @Deprecated
    public Long lGetListSize(String key) {
        return lSize(key);
    }

    /**
     * @deprecated 请使用 {@link #lIndex(String, long)}
     */
    @Deprecated
    public String lGetIndexString(String key, long index) {
        return lIndex(key, index);
    }

    /**
     * @deprecated 请使用 {@link #lIndex(String, long, Class)}
     */
    @Deprecated
    public <T> T lGetIndex(String key, long index, Class<T> type) {
        return lIndex(key, index, type);
    }

    /**
     * @deprecated 请使用 {@link #rPush(String, Object)}
     */
    @Deprecated
    public boolean lPush(String key, Object value) {
        return rPush(key, value);
    }

    /**
     * @deprecated 请使用 {@link #rPush(String, Object, long)}
     */
    @Deprecated
    public boolean lPush(String key, Object value, long time) {
        return rPush(key, value, time);
    }

    /**
     * @deprecated 请使用 {@link #rPushAll(String, List)}
     */
    @Deprecated
    public boolean lPushAll(String key, List<?> list) {
        return rPushAll(key, list);
    }

    /**
     * @deprecated 请使用 {@link #rPushAll(String, List, long)}
     */
    @Deprecated
    public boolean lPushAll(String key, List<?> list, long time) {
        return rPushAll(key, list, time);
    }

    /**
     * @deprecated 请使用 {@link #lSet(String, long, Object)}
     */
    @Deprecated
    public boolean lUpdateIndex(String key, long index, Object value) {
        return lSet(key, index, value);
    }

    /**
     * @deprecated 请使用 {@link #lRem(String, long, String)}
     */
    @Deprecated
    public Long lRemove(String key, long count, String value) {
        return lRem(key, count, value);
    }

    /**
     * @deprecated 请使用 {@link #lPop(String)}
     */
    @Deprecated
    public String lLeftPop(String key) {
        return lPop(key);
    }

    /**
     * @deprecated 请使用 {@link #lPop(String, Class)}
     */
    @Deprecated
    public <T> T lLeftPop(String key, Class<T> type) {
        return lPop(key, type);
    }

    /**
     * @deprecated 请使用 {@link #rPop(String)}
     */
    @Deprecated
    public String lRightPop(String key) {
        return rPop(key);
    }

    /**
     * @deprecated 请使用 {@link #rPop(String, Class)}
     */
    @Deprecated
    public <T> T lRightPop(String key, Class<T> type) {
        return rPop(key, type);
    }
}