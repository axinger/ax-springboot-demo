package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class OptionalTests {


    @Test
    @SuppressWarnings("all")
    void test() {
        String string = null;
        boolean empty = Optional.of(string).isEmpty();
        System.out.println("empty = " + empty);
    }

    @Test
    @SuppressWarnings("all")
    void test1() {
        String string = "123";
        Optional.ofNullable(string).filter(val -> val.length() > 2).ifPresentOrElse(val -> {
            System.out.println("val = " + val);
        }, () -> {
            System.out.println("长度没有2");
        });

        Optional.ofNullable(string)
                .filter(val -> val.length() > 2)
                .orElseThrow(() -> new RuntimeException("长度没有2"));
    }


    @Test
    @SuppressWarnings("all")
    void test2() {
        String string = null;
        boolean empty = Optional.ofNullable(string).isEmpty();
        System.out.println("empty = " + empty);
    }

    @Test
    @SuppressWarnings("all")
    public void test_1() {
        // isEmpty == !isPresent
        //       String str = "";
        String str = null;
        System.out.println("Optional.ofNullable(str).isEmpty() = " + Optional.ofNullable(str).isEmpty());
        System.out.println("Optional.ofNullable(str).isEmpty() = " + Optional.ofNullable(str).isPresent());

        Optional<String> optional = Optional.ofNullable(str);
        System.out.println(optional.isEmpty());

    }

    @Test
    @SuppressWarnings("all")
    public void test_add() {

        Long a = null;
        Long b = 1L;

        Long aLong = Optional.ofNullable(a + b).orElse(1L);
        System.out.println("aLong = " + aLong);
    }

    @Test
    @SuppressWarnings("all")
    public void test_list() {

        List<Integer> list = new ArrayList<>();
        {
            list.add(10);
        }
//        list.add("b");


        // 为指定的值创建一个Optional，如果指定的值为null，则返回一个空的Optional。
        /// 用stream试一试
        Optional.ofNullable(list).map(List::size).filter(val -> {
            System.out.println("filter val = " + val);
            return val > 0;
        }).ifPresent(val -> System.out.println("val = " + list.get(val - 1)));

        AtomicReference<Integer> value = new AtomicReference<>(0);
//        AtomicInteger value = new AtomicInteger();
        Optional.ofNullable(list).map(List::size).filter(val -> val > 0).ifPresent(val -> {
            value.set(list.get(0));
        });
        System.out.println("value = " + value.getClass());
        System.out.println("value = " + value.get());


    }


    @Test
    @SuppressWarnings("all")
    public void test_01() {

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

    @Test
    @SuppressWarnings("all")
    public void tes1() {

        Person person = null;

        Optional<Person> person1 = Optional.ofNullable(person);


        System.out.println("person1.orElse = " + person1.orElse(new Person()).getName());

        boolean present = person1.isPresent();
        if (present) {
            System.out.println("get = " + person1.get());
        }
    }


    @Test
    @SuppressWarnings("all")
    public void tes_optional_stream() {

        Person person = null;

        {
            person = new Person();
            person.setAge(10);
        }
        double sum = Optional.ofNullable(person)
                .stream()
                .mapToDouble(val -> val.getAge())
                .sum();
        System.out.println("sum = " + sum);


        List<Person> personList = null;
        {
            personList = new ArrayList<>();
            personList.add(person);
        }


        Double sum1 = Optional.ofNullable(personList)
                .stream()
                .flatMap(value -> value.stream())
                .mapToDouble(value -> value.getAge())
                .sum();
        System.out.println("sum1 = " + sum1);


    }


    @Test
    @SuppressWarnings("all")
    void test_get_1() {
        String val = null;

        final String orElse = Optional.ofNullable(val).orElse("jim");

        final String orElseGet = Optional.ofNullable(val).orElseGet(() -> {
            System.out.println("orElseGet========");
            return "jim";
        });

        System.out.println("orElse = " + orElse);
        System.out.println("orElseGet = " + orElseGet);
    }

    @Test
    @SuppressWarnings("all")
    void test_orElseThrow_1() throws Exception {
        Person person = null;
        Optional.ofNullable(person).orElseThrow(() -> new Exception("null值,就抛异常"));

        Optional.ofNullable(person).ifPresent(val -> {
            System.out.println("val = " + val);
        });

    }

    @Test
    @SuppressWarnings("all")
    void test_orElseThrow() throws Exception {
        Person person = Person.builder().name("nam").build();
        Optional.ofNullable(person).orElseThrow(() -> new Exception("null值,就抛异常"));

        Optional.ofNullable(person).ifPresent(val -> {
            System.out.println("val = " + val);
        });

    }

}
