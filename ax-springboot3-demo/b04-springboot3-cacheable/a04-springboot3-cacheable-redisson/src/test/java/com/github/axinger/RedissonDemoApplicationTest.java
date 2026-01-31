package com.github.axinger;

import com.github.axinger.model.Student;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedissonDemoApplicationTest {


    @Autowired
    RedissonClient redissonClient;


    @Test
    void test1() {
        // 获取所有的key
        redissonClient.getKeys().getKeys().forEach(key -> System.out.println(key));
    }

    /**
     * 2.2、字符串操作
     * Redisson 支持通过RBucket对象来操作字符串数据结构，通过RBucket实例可以设置value或设置value和有效期，简单样例如下！
     */
    @Test
    void test2() {
        // 字符串操作
        RBucket<String> rBucket = redissonClient.getBucket("strKey");
        // 设置value和key的有效期
        rBucket.set("张三", 60, TimeUnit.SECONDS);
        // 通过key获取
        Object strKey = redissonClient.getBucket("strKey").get();
        System.out.println("strKey = " + strKey);
    }

    /**
     * 2.3、对象操作
     * Redisson 支持将对象作为value存入redis，被存储的对象事先必须要实现序列化接口Serializable，否则会报错，简单样例如下！
     */
    @SneakyThrows
    @Test
    void test3() {


        List<Student> list = new ArrayList<>();
        {
            // Student对象
            Student student = new Student();
            student.setId(1L);
            student.setName("张三");
            student.setAge(18);
            list.add(student);
        }
        {
            // Student对象
            Student student = new Student();
            student.setId(1L);
            student.setName("李四");
            student.setAge(18);
            list.add(student);
        }

        // 对象操作
        RBucket<List<Student>> rBucket = redissonClient.getBucket("objKey");

        rBucket.delete();

        // 设置value和key的有效期
        rBucket.set(list, 600, TimeUnit.SECONDS);
        // 通过key获取value

        List<Student> list1 = rBucket.get();
        System.out.println("list1 = " + list1);

        TimeUnit.SECONDS.sleep(5);
    }

    /**
     * 2.4、哈希操作
     * Redisson 支持通过RMap对象来操作哈希数据结构，简单样例如下！
     */
    @Test
    void test4_getMap() {
        // 哈希操作
        RMap<String, String> rMap = redissonClient.getMap("mapkey");
// 设置map中key-value
        rMap.put("id", "123");
        rMap.put("name", "赵四");
        rMap.put("age", "50");

// 设置过期时间
        rMap.expire(30, TimeUnit.SECONDS);
// 通过key获取value
        System.out.println(redissonClient.getMap("mapkey").get("name"));
    }

    /**
     * 2.5、列表操作
     * Redisson 支持通过RList对象来操作列表数据结构，简单样例如下！
     */
    @Test
    void test5_getList() {
        // 字符串操作
        RList<Student> rList = redissonClient.getList("listkey");

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("张三");
        student1.setAge(18);
        rList.add(student1);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("李四");
        student2.setAge(19);
        rList.add(student2);

// 设置过期时间
        rList.expire(30, TimeUnit.SECONDS);
// 通过key获取value
        System.out.println(redissonClient.getList("listkey"));
    }

    /**
     * 2.6、集合操作
     * Redisson 支持通过RSet对象来操作集合数据结构，简单样例如下！
     */
    @Test
    void test6() {
        // 字符串操作
        RSet<Student> rSet = redissonClient.getSet("setkey");

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("张三");
        student1.setAge(18);
        rSet.add(student1);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("李四");
        student2.setAge(19);
        rSet.add(student2);

// 设置过期时间
        rSet.expire(30, TimeUnit.SECONDS);
// 通过key获取value
        System.out.println(redissonClient.getSet("setkey"));
    }

    /**
     * 2.6、有序集合操作
     * Redisson 支持通过RSortedSet对象来操作有序集合数据结构，在使用对象来存储之前，实体对象必须先实现Comparable接口，并重写比较逻辑，否则会报错，简单样例如下！
     */
    @Test
    void test6_2() {

        // 有序集合操作
        RSortedSet<Student> sortSetkey = redissonClient.getSortedSet("sortSetkey");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("李四");
        student2.setAge(19);
        sortSetkey.add(student2);

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("张三");
        student1.setAge(18);
        sortSetkey.add(student1);

// 通过key获取value
        System.out.println(redissonClient.getSortedSet("sortSetkey"));

    }

    /**
     * 2.7、布隆过滤器
     * 布隆过滤器（Bloom Filter）是 1970 年由布隆提出的。它实际上是一个很长的二进制向量和一系列随机映射函数。
     * <p>
     * 布隆过滤器可以用于检索一个元素是否在一个集合中。它的优点是空间效率和查询时间都比一般的算法要好的多，缺点是有一定的误识别率和删除困难。
     * <p>
     * Redisson 支持通过RBloomFilter对象来操作布隆过滤器，简单样例如下！
     */
    @Test
    void test7() {
        RBloomFilter rBloomFilter = redissonClient.getBloomFilter("seqId");
// 初始化预期插入的数据量为10000和期望误差率为0.01
        rBloomFilter.tryInit(10000, 0.01);
// 插入部分数据
        rBloomFilter.add("100");
        rBloomFilter.add("200");
        rBloomFilter.add("300");
// 设置过期时间
        rBloomFilter.expire(30, TimeUnit.SECONDS);
// 判断是否存在
        System.out.println(rBloomFilter.contains("300"));
        System.out.println(rBloomFilter.contains("200"));
        System.out.println(rBloomFilter.contains("999"));
    }

    /**
     * 2.8、分布式自增ID
     * ID 是数据的唯一标识，传统的做法是利用 UUID 和数据库的自增 ID。
     * <p>
     * 但由于 UUID 是无序的，不能附带一些其他信息，因此实际作用有限。
     * <p>
     * 随着业务的发展，数据量会越来越大，需要对数据进行分表，甚至分库。分表后每个表的数据会按自己的节奏来自增，这样会造成 ID 冲突，因此这时就需要一个单独的机制来负责生成唯一 ID，redis 原生支持生成全局唯一的 ID。
     * <p>
     * 简单样例如下！
     */
    @Test
    void test8() {
        final String lockKey = "test::order::no";
// 通过redis的自增获取序号
        RAtomicLong atomicLong = redissonClient.getAtomicLong(lockKey);
// 设置过期时间
        atomicLong.expire(24, TimeUnit.HOURS);
// 获取值
//         System.out.println(atomicLong.incrementAndGet());

        long incrementAndGet = redissonClient.getAtomicLong(lockKey)
                .incrementAndGet();
        System.out.println("incrementAndGet = " + incrementAndGet);

    }

    /**
     * 2.9、分布式锁
     * Redisson 最大的亮点，也是使用最多的功能，就是提供了强大的分布式锁实现，特点是：使用简单、安全！
     * <p>
     * 简单使用样例如下！
     */
    @Test
    void test9() {
// 获取锁对象实例
        final String lockKey = "abc";
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            // 尝试5秒内获取锁，如果获取到了，最长60秒自动释放
            boolean res = rLock.tryLock(5L, 60L, TimeUnit.SECONDS);
            if (res) {
                // 成功获得锁，在这里处理业务
                System.out.println("获取锁成功");
            }
        } catch (Exception e) {
            System.out.println("获取锁失败，失败原因：" + e.getMessage());
        } finally {
            // 无论如何, 最后都要解锁
            rLock.unlock();
        }
    }
}
