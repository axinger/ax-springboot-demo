package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.model.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class HashTests {


    @Autowired
    private RedisTemplate<String, Map<String, Object>> redisTemplateMap;


    @Autowired
    private ObjectMapper objectMapper;

    private User getUser(Integer id) {
        final User.Book book = User.Book.builder()
                .id(id)
                .name("海底两万里")
                .build();

        final User user = User.builder()
                .id(id)
                .name("jim")
                .age(21)
                .date(new Date())
                .books(List.of(book))
                .build();

        return user;
    }

    @Test
    void opsForHash_put() {
        final String key = "test-hash:User";
        Map<String, Object> map = new HashMap<>();
        map.put("name", "jim");
        map.put("age", 10);
        redisTemplateMap.opsForHash().putAll(key, map);
    }

    @Test
    void opsForHash_put2() {
        final String key = "test-hash:User";
        redisTemplateMap.opsForHash().put(key, "day", "2020-01-01");
    }

    @Test
    void map_set() {
        final String key = "test:map";
        redisTemplateMap.opsForHash().putAll(key, Map.of("name", "jim"));
    }

    @Test
    void map_put2() {
        final String key = "test:map";
        Object name = redisTemplateMap.opsForHash().get(key, "name");
        System.out.println("name = " + name);
    }


    @Test
    void opsForHash_put3() {
        final String key = "test11:hash:User";
        redisTemplateMap.opsForHash().put(key, "user", JSON.toJSONString(getUser(1)));
        Object name = redisTemplateMap.opsForHash().get(key, "user");
        System.out.println("name = " + name);
    }

    @Test
    void opsForHash_put4() {
        final String key = "test:hash:User";
        redisTemplateMap.opsForHash().put(key, "users", List.of(JSON.toJSONString(getUser(1))));
        // 存list ,无法取出
        Object name = redisTemplateMap.opsForHash().get(key, "users");
        System.out.println("name = " + name);
    }

    @Test
    void opsForHash_get() {
        final String key = "test:hash:User";
        Object name = redisTemplateMap.opsForHash().get(key, "name");
        System.out.println("name = " + name);
    }

    @SneakyThrows
    @Test
    void test_map() {

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

        final String key = "test:user:Map";
        System.out.println("key = " + key);

        Map map = objectMapper.readValue(objectMapper.writeValueAsString(user), Map.class);


        this.redisTemplateMap.opsForValue().set(key, map);

        final Map userMap = this.redisTemplateMap.opsForValue().get(key);

        System.out.println("userMap = " + userMap);


        User user1 = objectMapper.readValue(objectMapper.writeValueAsString(userMap), User.class);
        System.out.println("user1 = " + user1);
    }
}
