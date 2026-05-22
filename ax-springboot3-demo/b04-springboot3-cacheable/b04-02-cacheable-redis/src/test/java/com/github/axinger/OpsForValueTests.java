package com.github.axinger;

import cn.hutool.core.util.HashUtil;
import cn.hutool.core.util.StrUtil;
import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
public class OpsForValueTests {
    @Autowired
    private RedisTemplate<String, User> redisTemplateUser;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTemplate<String, List<User>> redisTemplateUserList;

    private User getUser(Integer id) {
        final User.Book book = User.Book.builder()
                .id(id)
                .name("海底两万里")
                .build();

        return User.builder()
                .id(id)
                .name("jim")
                .age(21)
                .date(new Date())
                // .localDateTime(LocalDateTime.now())
                .books(List.of(book))
                .build();
    }


    private String getUserKey(String key) {
        return StrUtil.format("test:value:{}:User", key);
    }

    @Test
    void opsForValue_set() {
        // redisTemplateUser.setValueSerializer(RedisSerializer.java());
        // redisTemplateUser.setValueSerializer(RedisSerializer.json());
        // 这个报错
        redisTemplateUser.opsForValue().set(getUserKey("1"), getUser(1), 10, TimeUnit.MINUTES);
        System.out.println("opsForValue");
    }

    @Test
    void opsForValue_get() {
        // redisTemplateUser.setValueSerializer(RedisSerializer.java());
        // redisTemplateUser.setValueSerializer(RedisSerializer.json());
        User user = redisTemplateUser.opsForValue().get(getUserKey("1"));
        System.out.println("user = " + user);
    }

    @Test
    void opsForValue_set2() {

        redisTemplateUserList.opsForValue().set(getUserKey("1and2"), List.of(getUser(1), getUser(2)), 10, TimeUnit.MINUTES);
        System.out.println("opsForValue");


        List<User> users = redisTemplateUserList.opsForValue().get(getUserKey("1"));
        System.out.println("user = " + users);
    }


    @Test
    void setIfAbsent() {
        // 当前key不存在，写入值, 并返回true; 当前key已经存在，不处理, 返回false;  Absent: 缺少的，
        Boolean ifAbsent = redisTemplateUser.opsForValue().setIfAbsent(getUserKey("1"), getUser(1));
        System.out.println("ifAbsent = " + ifAbsent);
    }

    @Test
    void setIfPresent() {
        // 当前key已经存在，写入值, 并返回true; 当前key不存在，不处理, 返回false;  ;Present: 存在的
        Boolean ifPresent = redisTemplateUser.opsForValue().setIfPresent(getUserKey("1"), getUser(1));
        System.out.println("ifPresent = " + ifPresent);
    }

    @Test
    void getAndSet() {
        // 获取原来key的value, 再将新的value写入
        User andSet = redisTemplateUser.opsForValue().getAndSet(getUserKey("1"), getUser(1));
        log.info("andSet = {}", andSet);
    }


    @Test
    void hash() {
        String id = "jim";
        System.out.println("HashUtil.apHash(id) = " + HashUtil.apHash(id));
        System.out.println("HashUtil.additiveHash(id,1) = " + HashUtil.additiveHash(id, 1));
        System.out.println("HashUtil.elfHash(id) = " + HashUtil.elfHash(id));
        System.out.println("HashUtil.fnvHash(id) = " + HashUtil.fnvHash(id));
    }

    @Test
    void setBit() {
        String key = "bitmap";
        redisTemplate.opsForValue().setBit(key, 0, true);
        redisTemplate.opsForValue().setBit(key, 1, true);
        redisTemplate.opsForValue().setBit(key, 4, true);
        redisTemplate.opsForValue().setBit(key, 2, true);
        redisTemplate.opsForValue().setBit(key, 5, true);

        System.out.println(redisTemplate.opsForValue().getBit(key, 2));
        System.out.println(redisTemplate.opsForValue().getBit(key, 3));
        System.out.println(redisTemplate.opsForValue().getBit(key, 5));

        // 获取当天签到人数
        Long count = redisTemplate.execute((RedisCallback<Long>) conn -> conn.bitCount(key.getBytes()));

        // 获取签到用户的列表
        List<Integer> userList = new ArrayList<>();
        Long pos = redisTemplate.execute((RedisCallback<Long>) conn -> conn.bitPos(key.getBytes(), true));
        // while (pos != -1) {
        //     userList.add(pos.intValue());
        //     Long finalPos = pos;
        //     pos = redisTemplate.execute((RedisCallback<Long>) conn ->{
        //       RedisZSetCommands.Range<Long> range = new RedisZSetCommands.Range();
        //
        //
        //        return conn.bitPos(key.getBytes(), true, range);
        //
        //     });
        // }

        Map<String, Object> result = new HashMap<>();
        result.put("count", count);
        result.put("userList", userList);
        System.out.println("result = " + result);


    }

    @Test
    void setBit_has() {
        String key = "sign:" + LocalDate.now();
        String id = "jim";
        int offset = HashUtil.fnvHash(id);
        redisTemplate.opsForValue().setBit(key, offset, true);
        System.out.println(redisTemplate.opsForValue().getBit(key, offset));
    }

    @Test
    void setBit_has_get() {
        String key = "sign:" + LocalDate.now();
        String id = "jim";
        int offset = HashUtil.fnvHash(id);
        System.out.println(redisTemplate.opsForValue().getBit(key, offset));
    }
}
