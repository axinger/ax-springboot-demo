package com.github.axinger.stream;

import com.github.axinger.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StreamSortedTests {


    @Test
    void test_Comparator() {


        List<User> list = List.of(
                User.builder()
                        .id(1)
                        .name("jim")
                        .area("安徽")
                        .build(),

                User.builder()
                        .id(2)
                        .name("tom")
                        .area("安徽")
                        .build(),

                User.builder()
                        .id(3)
                        .name("jim")
                        .build()

        );

        // 多重排序
        // list 无 null ,但对比的 key 可能null
        List<User> list1 = list.stream()
                .sorted(
                        Comparator.comparing(User::getArea, Comparator.nullsFirst(String::compareTo))
                                .reversed() // 这个逆序
                                .thenComparing(User::getId) // 这个正序
                )
                .toList();

        System.out.println("list 无 null ,但对比的 key 可能null = " + list1);

        User build3 = null;
        List<User> list2 = new ArrayList<>(Collections.singletonList(build3));
        list2.addAll(list1);
        System.out.println("list2 = " + list2);


        // list 可能有 null ,同时对比的 key 可能null, 与上面相同,就是加一个 Comparator.nullsFirst 判断
        List<User> list3 = list2.stream()
                .sorted(
                        Comparator.nullsFirst(
                                Comparator.comparing(User::getArea, Comparator.nullsFirst(String::compareTo))
                                        .reversed() // 这个逆序
                                        .thenComparing(User::getId) // 这个正序
                        )

                )
                .toList();
        // .sorted(Comparator.nullsFirst(Comparator.comparing(item -> item.getUid(), Comparator.nullsFirst(Long::compareTo))))

        System.out.println("list 可能有 null ,同时对比的 key 可能null = " + list3);
    }
}
