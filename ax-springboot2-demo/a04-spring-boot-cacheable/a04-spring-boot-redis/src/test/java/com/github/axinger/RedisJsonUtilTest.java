package com.github.axinger;

import com.github.axinger.model.User2;
import com.github.axinger.util.redis.RedisJsonUtil;
import com.github.axinger.util.redis.TypeConverter;
import com.github.axinger.util.redis.TypeConverterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class RedisJsonUtilTest {


    @Resource
    private RedisJsonUtil redisJsonUtil;

    // ==================== 类型转换器测试 ====================

    @Test
    @DisplayName("测试删除所有key")
    public void testDelALl() {

        Long result = redisJsonUtil.delPattern("test:*");
        System.out.println("result = " + result);
//        assertEquals(true, result > 0,"没有删除的key");
        Assert.isTrue(result > 0,"没有删除的key");
    }
    @Test
    @DisplayName("测试基本类型转换 - Boolean")
    public void testBooleanConverter() {
        String key = "test:boolean";

        // 测试 Boolean
        redisJsonUtil.set(key, true);
        Boolean result1 = redisJsonUtil.get(key, Boolean.class);
        assertEquals(true, result1);
        log.info("Boolean test passed: {}", result1);

        // 测试 boolean 基本类型
        redisJsonUtil.setString(key, "false");
        boolean result2 = redisJsonUtil.get(key, boolean.class);
        assertEquals(false, result2);
        log.info("boolean test passed: {}", result2);
    }

    @Test
    @DisplayName("测试基本类型转换 - Integer")
    public void testIntegerConverter() {
        String key = "test:integer";

        // 测试 Integer
        redisJsonUtil.setString(key, "12345");
        Integer result1 = redisJsonUtil.get(key, Integer.class);
        assertEquals(12345, result1);
        log.info("Integer test passed: {}", result1);

        // 测试 int 基本类型
        int result2 = redisJsonUtil.get(key, int.class);
        assertEquals(12345, result2);
        log.info("int test passed: {}", result2);
    }

    @Test
    @DisplayName("测试基本类型转换 - Long")
    public void testLongConverter() {
        String key = "test:long";

        redisJsonUtil.setString(key, "9876543210");
        Long result1 = redisJsonUtil.get(key, Long.class);
        assertEquals(9876543210L, result1);
        log.info("Long test passed: {}", result1);

        long result2 = redisJsonUtil.get(key, long.class);
        assertEquals(9876543210L, result2);
        log.info("long test passed: {}", result2);
    }

    @Test
    @DisplayName("测试基本类型转换 - Double")
    public void testDoubleConverter() {
        String key = "test:double";

        redisJsonUtil.setString(key, "3.14159");
        Double result1 = redisJsonUtil.get(key, Double.class);
        assertEquals(3.14159, result1, 0.00001);
        log.info("Double test passed: {}", result1);

        double result2 = redisJsonUtil.get(key, double.class);
        assertEquals(3.14159, result2, 0.00001);
        log.info("double test passed: {}", result2);
    }

    @Test
    @DisplayName("测试基本类型转换 - Float")
    public void testFloatConverter() {
        String key = "test:float";

        redisJsonUtil.setString(key, "2.718");
        Float result1 = redisJsonUtil.get(key, Float.class);
        assertEquals(2.718f, result1, 0.001f);
        log.info("Float test passed: {}", result1);

        float result2 = redisJsonUtil.get(key, float.class);
        assertEquals(2.718f, result2, 0.001f);
        log.info("float test passed: {}", result2);
    }

    @Test
    @DisplayName("测试大数类型转换 - BigDecimal")
    public void testBigDecimalConverter() {
        String key = "test:bigdecimal";

        BigDecimal value = new BigDecimal("12345.6789");
        redisJsonUtil.set(key, value);

        BigDecimal result = redisJsonUtil.get(key, BigDecimal.class);
        assertEquals(value, result);
        log.info("BigDecimal test passed: {}", result);

        // 测试字符串格式
        redisJsonUtil.setString(key, "99999.9999");
        BigDecimal result2 = redisJsonUtil.get(key, BigDecimal.class);
        assertEquals(new BigDecimal("99999.9999"), result2);
        log.info("BigDecimal string test passed: {}", result2);
    }

    @Test
    @DisplayName("测试大数类型转换 - BigInteger")
    public void testBigIntegerConverter() {
        String key = "test:biginteger";

        BigInteger value = new BigInteger("123456789012345678901234567890");
        redisJsonUtil.set(key, value);

        BigInteger result = redisJsonUtil.get(key, BigInteger.class);
        assertEquals(value, result);
        log.info("BigInteger test passed: {}", result);

        // 测试字符串格式
        redisJsonUtil.setString(key, "999999999999999999999999");
        BigInteger result2 = redisJsonUtil.get(key, BigInteger.class);
        assertEquals(new BigInteger("999999999999999999999999"), result2);
        log.info("BigInteger string test passed: {}", result2);
    }

    @Test
    @DisplayName("测试日期时间类型 - LocalDateTime")
    public void testLocalDateTimeConverter() {
        String key = "test:localdatetime";

        // 测试对象存储
        LocalDateTime now = LocalDateTime.now();
        redisJsonUtil.set(key, now);
        LocalDateTime result1 = redisJsonUtil.get(key, LocalDateTime.class);
        assertNotNull(result1);
        log.info("LocalDateTime object test passed: {}", result1);

        // 测试 ISO-8601 字符串格式
        redisJsonUtil.setString(key, "2024-01-01T10:30:00");
        LocalDateTime result2 = redisJsonUtil.get(key, LocalDateTime.class);
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 30, 0), result2);
        log.info("LocalDateTime ISO-8601 test passed: {}", result2);
    }

    @Test
    @DisplayName("测试日期时间类型 - LocalDate")
    public void testLocalDateConverter() {
        String key = "test:localdate";

        // 测试对象存储
        LocalDate today = LocalDate.now();
        redisJsonUtil.set(key, today);
        LocalDate result1 = redisJsonUtil.get(key, LocalDate.class);
        assertNotNull(result1);
        log.info("LocalDate object test passed: {}", result1);

        // 测试 ISO-8601 字符串格式
        redisJsonUtil.setString(key, "2024-01-01");
        LocalDate result2 = redisJsonUtil.get(key, LocalDate.class);
        assertEquals(LocalDate.of(2024, 1, 1), result2);
        log.info("LocalDate ISO-8601 test passed: {}", result2);
    }

    @Test
    @DisplayName("测试日期时间类型 - LocalTime")
    public void testLocalTimeConverter() {
        String key = "test:localtime";

        // 测试对象存储
        LocalTime now = LocalTime.now();
        redisJsonUtil.set(key, now);
        LocalTime result1 = redisJsonUtil.get(key, LocalTime.class);
        assertNotNull(result1);
        log.info("LocalTime object test passed: {}", result1);

        // 测试 ISO-8601 字符串格式
        redisJsonUtil.setString(key, "10:30:00");
        LocalTime result2 = redisJsonUtil.get(key, LocalTime.class);
        assertEquals(LocalTime.of(10, 30, 0), result2);
        log.info("LocalTime ISO-8601 test passed: {}", result2);
    }

    @Test
    @DisplayName("测试日期时间类型 - Date")
    public void testDateConverter() {
        String key = "test:date";

        // 测试对象存储
        Date now = new Date();
        redisJsonUtil.set(key, now);
        Date result1 = redisJsonUtil.get(key, Date.class);
        assertNotNull(result1);
        log.info("Date object test passed: {}", result1);

        // 测试时间戳格式
        long timestamp = System.currentTimeMillis();
        redisJsonUtil.setString(key, String.valueOf(timestamp));
        Date result2 = redisJsonUtil.get(key, Date.class);
        assertEquals(timestamp, result2.getTime());
        log.info("Date timestamp test passed: {}", result2);
    }

    @Test
    @DisplayName("测试日期时间类型 - Instant")
    public void testInstantConverter() {
        String key = "test:instant";

        // 测试对象存储
        Instant now = Instant.now();
        redisJsonUtil.set(key, now);
        Instant result1 = redisJsonUtil.get(key, Instant.class);
        assertNotNull(result1);
        log.info("Instant object test passed: {}", result1);

        // 测试时间戳格式
         key = "test:instant:timestamp";
        long timestamp = System.currentTimeMillis();
        redisJsonUtil.setString(key, String.valueOf(timestamp));
        Instant result2 = redisJsonUtil.get(key, Instant.class);
        assertEquals(timestamp, result2.toEpochMilli());
        log.info("Instant timestamp test passed: {}", result2);
    }

    @Test
    @DisplayName("测试 Hash 操作 - 基本类型")
    public void testHashBasicTypes() {
        String key = "test:hash:basic";

        // Integer
        redisJsonUtil.hSet(key, "count", 100);
        Integer count = redisJsonUtil.hGet(key, "count", Integer.class);
        assertEquals(100, count);

        // Boolean
        redisJsonUtil.hSet(key, "enabled", true);
        Boolean enabled = redisJsonUtil.hGet(key, "enabled", Boolean.class);
        assertEquals(true, enabled);

        // Double
        redisJsonUtil.hSet(key, "price", 99.99);
        Double price = redisJsonUtil.hGet(key, "price", Double.class);
        assertEquals(99.99, price, 0.01);

        log.info("Hash basic types test passed");
    }

    @Test
    @DisplayName("测试 Hash 操作 - 日期时间类型")
    public void testHashDateTimeTypes() {
        String key = "test:hash:datetime";

        // LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        redisJsonUtil.hSet(key, "lastLogin", now);
        LocalDateTime lastLogin = redisJsonUtil.hGet(key, "lastLogin", LocalDateTime.class);
        assertNotNull(lastLogin);

        // LocalDate
        LocalDate today = LocalDate.now();
        redisJsonUtil.hSet(key, "birthday", today);
        LocalDate birthday = redisJsonUtil.hGet(key, "birthday", LocalDate.class);
        assertNotNull(birthday);

        // BigDecimal
        BigDecimal amount = new BigDecimal("12345.67");
        redisJsonUtil.hSet(key, "amount", amount);
        BigDecimal savedAmount = redisJsonUtil.hGet(key, "amount", BigDecimal.class);
        assertEquals(amount, savedAmount);

        log.info("Hash datetime types test passed");
    }

    @Test
    @DisplayName("测试 List 操作 - 基本类型")
    public void testListBasicTypes() {
        String key = "test:list:basic";

        // 推入数据
        redisJsonUtil.rPush(key, 1);
        redisJsonUtil.rPush(key, 2);
        redisJsonUtil.rPush(key, 3);

        // 获取数据
        Integer first = redisJsonUtil.lIndex(key, 0, Integer.class);
        assertEquals(1, first);

        // 弹出数据
        Integer popped = redisJsonUtil.lPop(key, Integer.class);
        assertEquals(1, popped);

        log.info("List basic types test passed");
    }

    @Test
    @DisplayName("测试 List 操作 - 日期时间类型")
    public void testListDateTimeTypes() {
        String key = "test:list:datetime";

        // LocalDateTime
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now().plusHours(1);

        redisJsonUtil.rPush(key, time1);
        redisJsonUtil.rPush(key, time2);

        LocalDateTime retrieved = redisJsonUtil.lIndex(key, 0, LocalDateTime.class);
        assertNotNull(retrieved);

        LocalDateTime popped = redisJsonUtil.lPop(key, LocalDateTime.class);
        assertNotNull(popped);

        log.info("List datetime types test passed");
    }

    // 定义测试枚举
    enum TestStatus {
        ACTIVE, INACTIVE, PENDING
    }

    @Test
    @DisplayName("测试自定义枚举转换器")
    public void testCustomEnumConverter() {

        // 注册枚举转换器
        redisJsonUtil.registerConverter(new TypeConverter<TestStatus>() {
            @Override
            public TestStatus convert(String value) {
                return TestStatus.valueOf(value.toUpperCase());
            }

            @Override
            public Class<TestStatus> supportType() {
                return TestStatus.class;
            }

            @Override
            public int priority() {
                return 150;
            }
        });

        // 测试
        String key = "test:enum";
        redisJsonUtil.setString(key, "ACTIVE");
        TestStatus status = redisJsonUtil.get(key, TestStatus.class);
        assertEquals(TestStatus.ACTIVE, status);

        log.info("Custom enum converter test passed: {}", status);
    }

    @Test
    @DisplayName("测试类型转换器注册中心")
    public void testTypeConverterRegistry() {
        TypeConverterRegistry registry = TypeConverterRegistry.getInstance();

        // 检查内置类型支持
        assertTrue(registry.supports(Integer.class));
        assertTrue(registry.supports(Long.class));
        assertTrue(registry.supports(Boolean.class));
        assertTrue(registry.supports(LocalDateTime.class));
        assertTrue(registry.supports(BigDecimal.class));
        assertTrue(registry.supports(Date.class));

        // 检查基本类型支持
        assertTrue(registry.supports(int.class));
        assertTrue(registry.supports(long.class));
        assertTrue(registry.supports(boolean.class));

        log.info("TypeConverterRegistry test passed, total converters: {}", registry.size());
        log.info("Supported types: {}", registry.getSupportedTypes());
    }

    @Test
    @DisplayName("测试批量操作")
    public void testBatchOperations() {
        String hashKey = "test:batch:hash";

        // 批量设置 Hash
        Map<String, Object> data = new HashMap<>();
        data.put("count", 100);
        data.put("price", new BigDecimal("99.99"));
        data.put("enabled", true);
        data.put("lastUpdate", LocalDateTime.now());

        redisJsonUtil.hSetAll(hashKey, data);

        // 批量获取
        Integer count = redisJsonUtil.hGet(hashKey, "count", Integer.class);
        BigDecimal price = redisJsonUtil.hGet(hashKey, "price", BigDecimal.class);
        Boolean enabled = redisJsonUtil.hGet(hashKey, "enabled", Boolean.class);
        LocalDateTime lastUpdate = redisJsonUtil.hGet(hashKey, "lastUpdate", LocalDateTime.class);

        assertEquals(100, count);
        assertNotNull(price);
        assertEquals(true, enabled);
        assertNotNull(lastUpdate);

        log.info("Batch operations test passed");
    }

    @Test
    @DisplayName("测试复杂对象���储")
    public void testComplexObject() {
        String key = "test:complex";

        User2 dto = new User2();
        dto.setName("tom");

        redisJsonUtil.set(key, dto);
        User2 result = redisJsonUtil.get(key, User2.class);

        assertNotNull(result);
        assertEquals("tom", result.getName());

        log.info("Complex object test passed: {}", result);
    }

    @Test
    @DisplayName("测试 null 值处理")
    public void testNullHandling() {
        String key = "test:null";

        // 获取不存在的 key
        String result1 = redisJsonUtil.getString(key);
        assertNull(result1);

        Integer result2 = redisJsonUtil.get(key, Integer.class);
        assertNull(result2);

        LocalDateTime result3 = redisJsonUtil.get(key, LocalDateTime.class);
        assertNull(result3);

        log.info("Null handling test passed");
    }

    @Test
    @DisplayName("性能测试 - 基本类型转换")
    public void testPerformanceBasicTypes() {
        String key = "test:performance:basic";
        int iterations = 1000;

        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            redisJsonUtil.set(key, i);
            Integer result = redisJsonUtil.get(key, Integer.class);
            assertNotNull(result);
        }
        long end = System.currentTimeMillis();

        log.info("Performance test (basic types): {} iterations in {} ms, avg {} ms/op",
                iterations, (end - start), (end - start) / (double) iterations);
    }

    @Test
    @DisplayName("性能测试 - 日期时间类型转换")
    public void testPerformanceDateTimeTypes() {
        String key = "test:performance:datetime";
        int iterations = 1000;

        LocalDateTime now = LocalDateTime.now();
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            redisJsonUtil.set(key, now);
            LocalDateTime result = redisJsonUtil.get(key, LocalDateTime.class);
            assertNotNull(result);
        }
        long end = System.currentTimeMillis();

        log.info("Performance test (datetime types): {} iterations in {} ms, avg {} ms/op",
                iterations, (end - start), (end - start) / (double) iterations);
    }
}
