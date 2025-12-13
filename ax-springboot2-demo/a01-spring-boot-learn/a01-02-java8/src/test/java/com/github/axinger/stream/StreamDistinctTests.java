package com.github.axinger.stream;

import com.github.axinger.Person;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//去重
public class StreamDistinctTests {

    @Test
    void test1() {
        String[] arr1 = {"a", "b", "c", "d"};
        String[] arr2 = {"d", "e", "f", "g"};
        Stream<String> stream1 = Stream.of(arr1);
        Stream<String> stream2 = Stream.of(arr2);
        // concat:合并两个流 distinct 清楚的：去重
        List<String> newList = Stream.concat(stream1, stream2).distinct().toList();
        System.out.println("流合并去重 distinct：" + newList);
    }


    @Test
    void test2_对象去重() {


        List<Person> list = new ArrayList<>();

        list.add(Person.builder()
                .id(1)
                .name("张三")
                .build());
        list.add(Person.builder()
                .id(2)
                .name("李四")
                .build());
        list.add(Person.builder()
                .id(3)
                .name("王五")
                .build());
        list.add(Person.builder()
                .id(4)
                .name("赵六")
                .build());
        list.add(Person.builder()
                .id(4)
                .name("孙七")
                .build());

        // 单个属性去重
        ArrayList<Person> collect = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(Person::getId))), ArrayList::new));
        System.out.println("TreeSet去重 = " + collect);

        // 多个属性去重
        List<Person> collect2 = list.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(Person::getId)
                                        .thenComparing(Person::getName, Comparator.nullsLast(Comparator.naturalOrder()))
                                //保留最后一个
                        )), ArrayList::new));
        System.out.println("多个属性去重collect2 = " + collect2);


        List<Person> collect3 = list.stream()
                .filter(Objects::nonNull)  // 过滤掉 null 的 Person 对象
                .collect(Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(Person::getId)
                                .thenComparing(Person::getName, Comparator.nullsLast(Comparator.naturalOrder()))
                )))
                .stream()
                .toList();

        System.out.println("多个属性去重collect3 = " + collect3);


        List<Person> uniquePeople = list.stream()
                .collect(Collectors.toMap(Person::getId,
                        p -> p,
//                        Function.identity(),
                        (existing, replacement) -> existing)) // 使用id作为key，如果有重复则保留现有的元素
                .values() // 获取map的values，这将是一个去重后的列表
                .stream() // 将values集合再次转换为流，以便进一步操作或收集结果
                .toList(); // 收集结果为列表
        System.out.println("toMap去重 = " + uniquePeople);
    }


    //去重,保留想要的属性
    @Test
    void test3() {

        List<Person> list2 = List.of(

                Person.builder()
                        .id(1)
                        .name("jim")
                        .age(10)
                        .address("安徽")
                        .build(),

                Person.builder()
                        .id(2)
                        .name("tom")
                        .age(10)
                        .address("安徽")
                        .build(),

                Person.builder()
                        .id(3)
                        .name("lili")
                        .age(11)
                        .address("江苏")
                        .build(),

                Person.builder()
                        .id(4)
                        .name("moni")
                        .age(11)
                        .address("安徽")
                        .build(),

                Person.builder()
                        .id(5)
                        .name("luck")
                        .age(11)
                        .address(null)
                        .build()
        );


        List<Person> collect3 = list2.stream()
                .filter(Objects::nonNull)  // 过滤掉 null 的 Person 对象
                .collect(Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(Person::getAge)

                )))
                .stream()
                .toList();

        System.out.println("多个属性去重 collect3 = " + collect3);


        List<Person> collect4 = list2.stream()
                // 在 Stream 上进行过滤操作
                .sorted(Comparator.comparing((Person person) -> {
//                    if ("安徽".equals(person.getAddress())) {
//                        return -1;
//                    } else if ("江苏".equals(person.getAddress())) {
//                        return 0;
//                    } else {
//                        return 1;
//                    }

                    // 把想要的,排序最前面, TreeSet 会先插入
                    if ("安徽".equals(person.getAddress())) {
                        return -1;
                    } else {
                        return 1;
                    }
                }).thenComparing(Comparator.nullsLast(Comparator.comparing(Person::getName))))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Person::getAge))))
                .stream()
                .toList();
        System.out.println("多个属性去重 collect4 = " + collect4);
    }


    @Test
    public void test1_合并() {
        String[] arr1 = {"a", "b", "c", "d"};
        String[] arr2 = {"d", "e", "f", "g"};

        Stream<String> stream1 = Stream.of(arr1);
        Stream<String> stream2 = Stream.of(arr2);
        // concat:合并两个流 distinct 清楚的：去重
        List<String> newList = Stream.concat(stream1, stream2).distinct().collect(Collectors.toList());
        System.out.println("流合并去重 distinct：" + newList);


        // limit：限制从流中获得前n个数据
        System.out.println("Stream.iterate(1, x -> x + 2) = " + Stream.iterate(1, x -> x + 2));

        List<Integer> collect = Stream.iterate(1, x -> x + 2).limit(10).collect(Collectors.toList());
        System.out.println("limit：" + collect);

        List<Integer> collect1 = Stream.iterate(1, x -> x + 1).limit(10).collect(Collectors.toList());
        System.out.println("limit collect1：" + collect1);

        // skip：跳过前n个数据
        List<Integer> collect2 = Stream.iterate(1, x -> x + 2).skip(3).limit(5).collect(Collectors.toList());
        System.out.println("skip：" + collect2);
    }

    @Test
    public void test3_map去重() {

        List<Map<String, Object>> list = new ArrayList<>();

        list.add(Map.of("id", 1, "name", "张三"));
        list.add(Map.of("id", 2, "name", "李四"));
        list.add(Map.of("id", 3, "name", "王五"));
        list.add(Map.of("id", 4, "name", "王五"));


        List<Map<String, Object>> list1 = list.stream()
                .collect(Collectors.toMap(
                        value -> value.get("name"),
                        Function.identity(),
                        (existing, replacement) -> {
                            /// 取值id最大的
                            int existingId = (Integer) existing.get("id");
                            int replacementId = (Integer) replacement.get("id");
                            return existingId > replacementId ? existing : replacement;
                        }
                ))
                .values()
                .stream().toList();
        System.out.println("list1 = " + list1);

    }
}
