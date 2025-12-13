package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class ObjectTest {

    @Test
    void test4() {
        String str = "";
    }

    @Test
    void test5() {

        Person person = new Person();
        person.setName("jim");
        person.setAge(10);
        System.out.println("person = " + person);

        String name = "jim";
        System.out.println("name = " + name);
        test6(person);
        test7(name);
        test8(person);
        System.out.println("==============================================");
        System.out.println("person2 = " + person);
        System.out.println("name = " + name);
    }

    /// java是值引用
    @Test
    void test51() {

        Person person = new Person();
        person.setName("jim");
        person.setAge(10);

        Map<String, Object> params = new HashMap<>();
        params.put("tip", "123");

        Dog dog = new Dog();
        dog.setName("golf");
        params.put("dog", dog);
        person.setParams(params);
        System.out.println("person = " + person);

        test8(person);
        System.out.println("==============================================");
        System.out.println("person2 = " + person);
    }

    // 可以修改原对象的值
    void test6(Person person) {
        person.setName("tom");
    }

    // 无法修改原String的值
    void test7(String name) {
        name = "tom";
        System.out.println("test7 name = " + name);
    }

    void test8(Person person) {
        String name = person.getName();
        name = "lili"; //不能修改

        Object tip = person.getParams().get("tip");
        tip = "456"; //不能修改

        Dog dog = (Dog) person.getParams().get("dog");
        dog.setName("lucy"); //可以修改
    }

    @Test
    void test52() {
        Map<String, Object> params = new HashMap<>();
        params.put("1", "A");
        params.put("2", "B");

        /// 不能删除
//        for (String s : params.keySet()) {
//            params.remove(s);
//        }

        /// 不能删除
//        params.keySet().forEach(s -> params.remove(s));

        /// 不能删除
//        params.keySet().stream().forEach(s -> params.remove(s));


        /// 能删除
//        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Object> next = iterator.next();
//            if (next.getKey().equals("1")) {
//                iterator.remove();
//            }
//        }


        /// 能删除,先收集
//        List<String> keysToRemove = new ArrayList<>();
//        params.forEach((key, value) -> {
//            if ("1".equals(key)) {
//                keysToRemove.add(key); // 收集要删除的键
//            }
//        });
//        keysToRemove.forEach(params::remove); // 一次性删除所有收集到的键


        /// 能删除
//        params.entrySet().removeIf(entry -> entry.getKey() != "1");   //删除符合条件的Entry
        params.keySet().removeIf(key -> key != "1");                  //删除符合条件的Entry
//        params.values().removeIf(value -> value.contains("1"));     //删除符合条件的Entry

        System.out.println("params = " + params);
    }
}
