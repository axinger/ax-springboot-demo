package com.github.axinger;

import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
public class ListTest {

    @Autowired
    private RedisTemplate<String, User> redisTemplateList;


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
    void opsForList_leftPush() {
        // 将所有指定的值插入存储在键的列表的头部
        final String key = "test:list:User";

        Long aLong = redisTemplateList.opsForList().leftPush(key, getUser(1));
        System.out.println("aLong = " + aLong);

        Long aLon2 = redisTemplateList.opsForList().leftPush(key, getUser(2));
        System.out.println("aLong = " + aLon2);
    }

    @Test
    void opsForList_pop() {
        // 弹出最左边的元素，弹出之后该值在列表中将不复存在
        final String key = "test:list:User";
        User user = redisTemplateList.opsForList().rightPop(key);
        System.out.println("user = " + user);

        User user2 = redisTemplateList.opsForList().leftPop(key);
        System.out.println("user2 = " + user2);
    }

    @Test
    void opsForList_range() {
        // 不会越界
        final String key = "test:list:User";
        List<User> range = redisTemplateList.opsForList().range(key, 1, 3);
        System.out.println("range = " + range);
    }

    @Test
    void opsForList_index() {
        // 弹出最左边的元素，弹出之后该值在列表中将不复存在
        final String key = "test:list:User";
        User index = redisTemplateList.opsForList().index(key, 0);
        System.out.println("index = " + index);

        User index1 = redisTemplateList.opsForList().index(key, 1);
        System.out.println("index = " + index1);
    }

}
