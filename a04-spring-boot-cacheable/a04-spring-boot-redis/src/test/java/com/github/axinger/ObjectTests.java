package com.github.axinger;


import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.model.Order;
import com.github.axinger.model.User;
import com.github.axinger.util.JsonRedisUtil;
import com.github.axinger.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class ObjectTests {

    private static final String SERIAL_NUM = "order:serialNo:";
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper; // 注入全局 ObjectMapper
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JsonRedisUtil jsonRedisUtil;

    @Test
    void test1() {

        final User.Book book = User.Book.builder()
                .id(1)
                .name("海底两万里")
                .build();

        final User user = User.builder()
                .id(1)
                .name("jim")
                .age(21)
                .date(new Date())
                .books(List.of(book))
                .build();

        final String key = "test:user:1:User";
        redisTemplate.delete(key);
        this.redisTemplate.opsForValue().set(key, user, 1, TimeUnit.HOURS);

        // 设置键的字符串值并返回其旧值
//        User andSet = this.redisTemplateUser.opsForValue().getAndSet(key, user);
//
//        System.out.println("andSet = " + andSet);

        // 可以直接存, 不能直接取
        // final Map user1 = (Map) this.redisTemplateUser.opsForValue().get(key);
        // System.out.println("user1 = " + user1);

//        {
//
//            final User user2 = (User) this.redisTemplate.opsForValue().get(key);
//
//            Assertions.assertNotNull(user2);
//            System.out.println("user2 getClass= " + user2.getClass());
//            System.out.println("user2 = " + user2);
//
//        }

        {

            User user1 = redisUtil.get(key, User.class);
            System.out.println("user1 = " + user1);

        }
        {
            final Object user3 = this.redisTemplate.opsForValue().get(key);
            Assertions.assertNotNull(user3);
            System.out.println("user2 getClass= " + user3.getClass());
            System.out.println("user3 = " + user3);

            User user1 = JSON.parseObject(JSON.toJSONString(user3), User.class);
            System.out.println("user1 = " + user1);

            // 否则从 LinkedHashMap 或 JSON 字符串转
            try {
                String json = objectMapper.writeValueAsString(user3);
                User user2 = objectMapper.readValue(json, User.class);
                System.out.println("user2 = " + user2);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize to User", e);
            }
        }
    }

    @Test
    void test2() {

        final User.Book book = User.Book.builder()
                .id(1)
                .name("海底两万里")
                .build();

        final User user = User.builder()
                .id(1)
                .name("jim")
                .age(21)
                .date(new Date())
                .updateTime(LocalDateTime.now())
                .books(List.of(book))
                .build();

        final String key = "test:json:user:1:User";
        jsonRedisUtil.delete(key);
        jsonRedisUtil.set(key, user, Duration.ofHours(1));


        User user1 = jsonRedisUtil.get(key, User.class);
        System.out.println("user1 = " + user1);
    }

    /**
     * 自增流水号
     */
    @Test
    void orderSerialNo() {
        for (int i = 0; i < 100; i++) {
            testNum();
        }
    }

    void testNum() {
        LocalDateTime dateTime = LocalDateTime.now();
        dateTime = dateTime.plusDays(1);
        final String currentDate = LocalDateTimeUtil.format(dateTime, "yyyy-MM-dd");
        String key = SERIAL_NUM + currentDate;
        // 过期时间 60*60*24
        long incr = redisTemplate.opsForValue().increment(key, 1);
        // 左对齐
        String value = StrUtil.padPre(String.valueOf(incr), 6, "0");
        // 然后把 时间戳和优化后的 ID 拼接
        String code = StrUtil.format("{}-{}", currentDate, value);
        System.out.println("code = " + code);
    }

    @Test
    public void testOrder() {
        final String key = "test-order:1";
        Order order = Order.builder()
                .id("1")
                .name("jim")
                .dateTime(LocalDateTime.now())
                .build();
        redisTemplate.opsForValue().set(key, order);
    }

    @Test
    public void testOrder2() {
        final String key = "test-order:1";

        Object o = redisTemplate.opsForValue().get(key);
        System.out.println("o = " + o);
        Class<?> aClass = o.getClass();
        System.out.println("aClass = " + aClass);

        if (o instanceof Order order) {
            System.out.println("order = " + order);
        }
    }

}
