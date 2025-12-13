package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
public class BytesTests {


    final String key = "test:user:bytes:1";

    @Resource(name = "bytesRedisTemplate")
    private RedisTemplate<String, byte[]> bytesRedisTemplate;

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
    public void test31() {

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
        bytesRedisTemplate.opsForValue().set(key, JSON.toJSONString(user).getBytes());
    }

    @Test
    public void test32() {

        byte[] bytes = bytesRedisTemplate.opsForValue().get(key);
        if (bytes != null) {
            String s = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("s = " + s);
        }

    }

    @Test
    public void test33() {
        final String key = "test31:hash:bytes:1";
        bytesRedisTemplate.opsForHash().put(key, "user", JSON.toJSONString(getUser(1)).getBytes());
        byte[] user = (byte[]) bytesRedisTemplate.opsForHash().get(key, "user");
        Assert.notNull(user, "1111111");
        System.out.println("name = " + new String(user));
    }

}
