package com.github.axinger.stream;

import com.github.axinger.Person;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest_sorted {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<Person>();

        personList.add(Person.builder()
                .id(1)
                .build());
        personList.add(Person.builder()
                .id(1)
                .build());
        personList.add(Person.builder()
                .id(1)
                .build());
        personList.add(Person.builder()
                .id(1)
                .build());
        personList.add(Person.builder()
                .id(1)
                .build());

        // 按工资升序排序（自然排序）
        List<String> newList = personList.stream().sorted(Comparator.comparing(Person::getAge)).map(Person::getName)
                .collect(Collectors.toList());
        // 按工资倒序排序
        List<String> newList2 = personList.stream().sorted(Comparator.comparing(Person::getAge).reversed())
                .map(Person::getName).collect(Collectors.toList());
        // 多种排序 先按工资再按年龄升序排序
        //避免null
        List<String> newList3 = personList.stream()
                .sorted(Comparator.comparing(Person::getAge, Comparator.nullsLast(Comparator.naturalOrder())) // 判断null
                        .thenComparing(Person::getAge, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(Person::getName)
                .collect(Collectors.toList());
        // 先按工资再按年龄自定义排序（降序）
        List<String> newList4 = personList.stream().sorted((p1, p2) -> {
            if (p1.getAge() == p2.getAge()) {
                return p2.getAge() - p1.getAge();
            } else {
                return p2.getAge() - p1.getAge();
            }
        }).map(Person::getName).collect(Collectors.toList());

        System.out.println("按工资升序排序：" + newList);
        System.out.println("按工资降序排序：" + newList2);
        System.out.println("先按工资再按年龄升序排序：" + newList3);
        System.out.println("先按工资再按年龄自定义降序排序：" + newList4);
    }
}
