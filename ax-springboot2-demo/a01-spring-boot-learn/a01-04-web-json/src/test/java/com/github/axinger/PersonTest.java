package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.entity.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

class PersonTest {

    @Test
    void test1() throws Exception {

        final com.github.axinger.entity.Person person = com.github.axinger.entity.Person.builder()
                .string2("jim")
                .list2(Arrays.asList("a", "b"))
                .map2(new HashMap<>() {{
                    put("age", 10);
                }})
                .aBoolean2(true)
                .localDateTime(LocalDateTime.now())
                .date(new Date())
                .aDouble(2.12433)
                .aFloat(2.12533f)
                .aLong(2222L)
                .aTransient("transient修改不被序列化")
                .aaTransient("transient修改不被序列化")
                .build();

        System.out.println("person = " + person);


        final String personStr = JSON.toJSONString(person);
        System.out.println("personStr = " + personStr);

        final List<Person> list = Collections.singletonList(person);
        final String listStr = JSON.toJSONString(list);
        System.out.println("listStr = " + listStr);

    }

}
