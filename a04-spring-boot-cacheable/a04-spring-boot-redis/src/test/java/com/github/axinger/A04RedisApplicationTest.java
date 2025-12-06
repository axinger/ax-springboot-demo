package com.github.axinger;

import com.github.axinger.model.Order;
import com.github.axinger.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@SpringBootTest
public class A04RedisApplicationTest {


    @Autowired
    @Qualifier("myJsonRedisTemplate")
    private RedisTemplate<String, User> redisTemplateUser;

    @Autowired
    private RedisTemplate<String, Order> redisTemplateOrder;


    @Test
    void test1_redisTemplateUser() {

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

        final String key = "test:bean:user:1:User";
        redisTemplateUser.delete(key);
        this.redisTemplateUser.opsForValue().set(key, user, 1, TimeUnit.HOURS);

        // 设置键的字符串值并返回其旧值
//        User andSet = this.redisTemplateUser.opsForValue().getAndSet(key, user);
//
//        System.out.println("andSet = " + andSet);

        // 可以直接存, 不能直接取
        // final Map user1 = (Map) this.redisTemplateUser.opsForValue().get(key);
        // System.out.println("user1 = " + user1);


        final User user2 = this.redisTemplateUser.opsForValue().get(key);
//
        System.out.println("user2 = " + user2);
    }


    @Test
    void test_map_to_user() {

        final String key = "test:user:1:Map";
        User user = redisTemplateUser.opsForValue().get(key);
        System.out.println("user = " + user);
    }


    @Test
    void test_Cacheable() {
        System.out.println("getByKey(1L) = " + getByKey(1L));
    }

    @Cacheable(key = "#id")
    public String getByKey(final Long id) {
        return "我的jim" + id;
    }


    @Test
    public void testOrder21() {
        final String key = "test-order:2";
        Order order = Order.builder()
                .id("1")
                .name("jim")
                .dateTime(LocalDateTime.now())
                .build();
        redisTemplateOrder.opsForValue().set(key, order);

        Object order1 = redisTemplateOrder.opsForValue().get(key);
        System.out.println("order1 = " + order1);
        if (order1 instanceof Order order2) {
            System.out.println("order2 = " + order2);
        }
    }


}
