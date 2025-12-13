package com.github.axinger;

import com.github.axinger.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RedisTemplateTests {
    @Autowired
    private RedisTemplate<Object, List<Student>> redisTemplate;

    @Test
    void test1() {

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
            student.setDateTime(LocalDateTime.now());
            list.add(student);
        }
        redisTemplate.opsForValue().set("test1", list);


        List<Student> list1 = redisTemplate.opsForValue().get("test1");
        System.out.println("list1 = " + list1);
    }
}
