package com.github.axinger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName Optional.java
 * @Description TODO
 * @createTime 2022年01月24日 19:27:00
 * <p>
 * http://www.ibloger.net/article/3209.html
 */

public class OptionalDemo {

    public static void main(String[] args) {

        User user = null;

        Optional<User> optional = Optional.ofNullable(user);

        System.out.println("isPresent = " + optional.isPresent());
        if (optional.isPresent()) {
            System.out.println("get = " + optional.get());
        }

        // 如果传入参数为null，则抛出NullPointerException 。
//        Optional.of(user).ifPresent(v ->{
//            System.out.println("optional of ifPresent get = " + v);
//        });

        // 为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。
        Optional.ofNullable(user).ifPresent(v -> {
            System.out.println("optional ofNullable ifPresent get = " + v);
        });


        User user2 = new User();


        Optional<User> optional2 = Optional.ofNullable(user2);
        System.out.println("optional2 isPresent = " + optional2.isPresent());

        if (optional2.isPresent()) {
            System.out.println("optional2 get = " + optional2.get());
        }

        Optional.ofNullable(user2).ifPresent(v -> {
            System.out.println("optional2 get = " + v);
        });

        Optional.of(user2).ifPresent(v -> {
            System.out.println("optional2 of ifPresent get = " + v);
        });


        Optional<String> optional3 = Optional.ofNullable(user2).map(User::getName);
        System.out.println("optional2 isPresent = " + optional3.isPresent());
        if (optional3.isPresent()) {
            // 报错
            System.out.println("optional3 get = " + optional3.get());
        }

        user2.setCity(new City());
        Optional<City> optional4 = Optional.ofNullable(user2).map(User::getCity);
        System.out.println("optional4 isPresent = " + optional4.isPresent());
        if (optional4.isPresent()) {
            System.out.println("optional4 get = " + optional4.get());
        }
        Optional.ofNullable(user2).map(User::getCity).ifPresent(v -> {
            System.out.println("optional4 ifPresent get = " + v);
        });

        Optional.of(user2).map(User::getCity).ifPresent(v -> {
            System.out.println("optional4 of ifPresent get = " + v);
        });


        Optional<String> optional5 = Optional.ofNullable(user2).map(User::getCity).map(City::getAddress);
        System.out.println("optional5 isPresent = " + optional5.isPresent());
        if (optional5.isPresent()) {
            System.out.println("optional5 get = " + optional5.get());
        }

        Optional<String> optional5_2 = Optional.of(Optional.ofNullable(user2).map(User::getCity).map(City::getAddress).orElse("jim"));

        System.out.println("optional5_2 isPresent = " + optional5_2.isPresent());
        System.out.println("optional5_2 get = " + optional5_2.get());
        if (optional5_2.isPresent()) {
            System.out.println("optional5_2 get = " + optional5_2.get());
        }


        user2.getCity().setAddress("大街上");

        Optional<String> optional6 = Optional.ofNullable(user2).map(User::getCity).map(City::getAddress);
        System.out.println("optional6 isPresent = " + optional6.isPresent());
        if (optional6.isPresent()) {
            System.out.println("optional6 get = " + optional6.get());
        }

        Optional.ofNullable(user2).map(User::getCity).map(City::getAddress).ifPresent(v -> {
            System.out.println("optional6 ifPresent get = " + v);
        });


        Map<String, Object> map = new HashMap<>(2);
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        System.out.println("map = " + map);

    }


}
