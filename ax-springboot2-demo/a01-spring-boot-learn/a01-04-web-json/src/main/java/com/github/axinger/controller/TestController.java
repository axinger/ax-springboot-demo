package com.github.axinger.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.axing.common.advice.annotation.IgnoreResponseAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axinger.dto.UserDTO;
import com.github.axinger.entity.EmptyModel;
import com.github.axinger.entity.Person;
import com.github.axinger.entity.Pig;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author xing
 */


@RestController
public class TestController {

    @Autowired
    private Pig pig;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/test1")
    public Object count1() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", LocalDate.now());
        map.put("date2", LocalDateTime.now());
        map.put("date3", LocalTime.now());
        map.put("date4", ZonedDateTime.now());
        return map;
    }

    @GetMapping(value = "/json")
    public Object json() {

        Map map = new HashMap();
        map.put("localDateTime", LocalDateTime.now());
        map.put("localDate", LocalDate.now());
        map.put("localTime", LocalTime.now());
        map.put("date", new Date());

        final Person person = Person.builder()
                .string2("jim")
                .list2(Lists.newArrayList("a", "b"))
                .map2(new HashMap<String, Object>() {{
                    put("age", 10);
                }})
                .aBoolean2(true)
                .localDateTime(LocalDateTime.now())
                .date(new Date())
                .aDouble(2.12433)
                .aFloat(2.12533f)
                .aLong(2222L)
                .build();

        Person person1 = new Person();
        person1.setString2("tom");
        final List<Person> list = Lists.newArrayList(person, person1);

        map.put("personList", list);

        EmptyModel emptyModel = new EmptyModel();
        map.put("emptyModel", emptyModel);


        return map;
    }

    @GetMapping(value = "/json1")
    @IgnoreResponseAdvice(value = false)
    public Object json1() {

        final Person person = Person.builder()
                .string2("jim")
                .list2(Lists.newArrayList("a", "b"))
                .map2(new HashMap<String, Object>() {{
                    put("age", 10);
                }})
                .aBoolean(true)
                .aBoolean2(true)
                .localDateTime(LocalDateTime.now())
                .localDateTime2(LocalDateTime.now())
                .localDate2(LocalDate.now())

                .date(new Date())
                .aDouble(2.12433)
                .aFloat(2.12533f)
                .aLong(2222L)
                .build();
        return person;
    }

    @GetMapping(value = "/pig")
    public void pig() {

        System.out.println("new Pig() = " + new Pig());
        System.out.println("pig = " + pig);


        System.out.println("spring容器单例 objectMapper = " + objectMapper);
        System.out.println("jdk 不是单例 new ObjectMapper() = " + new ObjectMapper());

    }

    @GetMapping(value = "/assert")
    public Map pig(Integer val) {
        Assert.isTrue(NumberUtil.isEven(val), "不是偶数{}{}", 1, 2);
        Map map = new HashMap();
        map.put("localDateTime", LocalDateTime.now());
        return map;
    }


    @PostMapping(value = "/age")
    public Object json1(@RequestBody @Validated UserDTO userDTO) {
        return userDTO;
    }
}

