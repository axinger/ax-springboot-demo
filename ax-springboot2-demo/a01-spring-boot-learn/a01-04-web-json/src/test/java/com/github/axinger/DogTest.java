package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.axinger.entity.Dog;
import com.github.axinger.entity.Person;
import org.junit.jupiter.api.Test;

class DogTest {

    @Test
    void test1() throws Exception {
        final Dog dog = new Dog();
        dog.setString("不使用builder");
        dog.setString2("str2");
        dog.setATransient("首部1个小写字母,transient不被序列化,失效");
        dog.setAaTransient("首部2个以上小写字母,transient修改不被序列化,正常");
        System.out.println("JSON.toJSONString(dog) = " + JSON.toJSONString(dog));

        final Dog dog1 = Dog.builder()
                .string("使用builder")
                .aTransient("首部1个小写字母,transient不被序列化,失效")
                .aaTransient("首部2个以上小写字母,transient修改不被序列化,正常")
                .build();
        System.out.println("builder JSON.toJSONString(dog1) = " + JSON.toJSONString(dog1));


        com.github.axinger.entity.Person person = new com.github.axinger.entity.Person();
        person.setAaTransient("transient修改不被序列化");
        System.out.println("JSON.toJSONString(person) = " + JSON.toJSONString(person));


        final com.github.axinger.entity.Person person2 = Person.builder()
                .aTransient("transient修改不被序列化")
                .aaTransient("transient修改不被序列化")
                .build();
        System.out.println("builder JSON.toJSONString(person2) = " + JSON.toJSONString(person2));

    }

    @Test
    void test2() throws Exception {

        final Dog dog = new Dog();
        dog.setString("不使用builder");
        dog.setString2("str2");
        dog.setName2("name2");
        dog.setATransient("首部1个小写字母,JsonIgnore不被序列化,正常");
        dog.setATransient2("首部1个小写字母,transient不被序列化,失效");
        dog.setAaTransient("首部2个以上小写字母,transient修改不被序列化,正常");
        dog.setAaTransient2("首部2个以上小写字母,transient修改不被序列化,正常");
        String s = JSON.toJSONString(dog);
        System.out.println("s = " + s);

        Dog dog1 = JSON.parseObject(s, Dog.class);

        // 内部调用  return JSON.parseObject(text, clazz);
        Dog dog2 = JSONObject.parseObject(s, Dog.class);

        System.out.println("dog1 = " + dog1);
        System.out.println("dog2 = " + dog2);
    }

}
