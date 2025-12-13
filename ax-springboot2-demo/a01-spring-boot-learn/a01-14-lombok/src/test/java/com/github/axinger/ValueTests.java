package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.junit.Test;

public class ValueTests {

    @Test
    public void test1() {

        User user = new User("", 12);
        int age = user.getAge();
        String name = user.getName();


        System.out.println("name = " + name);
        System.out.println("age = " + age);
        System.out.println("ValueTests.test1");

    }

    /*
    提供 @Value注解，可以产生一个不可变的 entity（final 类、final 字段、只能 getter、只能通过全参构造方法初始化），且仅能配置静态方法名称。
     */
    @Value
    @Data
    @AllArgsConstructor
//    @NoArgsConstructor //不能无参构造器
    public static class User {
        String name;
        int age;
    }
}
