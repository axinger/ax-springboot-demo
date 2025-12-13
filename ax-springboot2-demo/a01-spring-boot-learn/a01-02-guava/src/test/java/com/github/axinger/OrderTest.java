package com.github.axinger;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName GuavaOrderTest.java
 * @description TODO
 * @createTime 2022年03月21日 13:43:00
 */

public class OrderTest {


    @Test
    void test_Ordering() {

        Ordering<Comparable> natural = Ordering.natural();
        System.out.println("natural.max(1, 2) = " + natural.max(1, 2));// 2
        System.out.println("natural.min(1, 2) = " + natural.min(1, 2));

        List<Integer> list2 = Lists.newArrayList(5, 9, 3, 7, 4, null);

        // null 最前面
        final Ordering<Comparable> ordering = natural.nullsFirst();

        Collections.sort(list2, ordering);
        System.out.println("Arrays.asList(list) = " + Arrays.toString(list2.toArray()));
    }

    @Test
    void test_Comparator() {

        UserInfo build0 = UserInfo.builder().uid(1L).gender(1).build(); // 这个不要uid2
        UserInfo build1 = UserInfo.builder().uid(2L).uid2(2L).gender(0).build();
        UserInfo build2 = UserInfo.builder().uid(3L).uid2(2L).gender(0).build();

        List<UserInfo> list = List.of(build0, build1, build2);

        // 多重排序
        // list 无 null ,但对比的 key 可能null
        List<UserInfo> list1 = list.stream()
                .sorted(
                        Comparator.comparing(UserInfo::getUid2, Comparator.nullsFirst(Long::compareTo)).reversed() // 这个逆序
                                .thenComparing(UserInfo::getUid) // 这个正序
                )
                .toList();

        System.out.println("list 无 null ,但对比的 key 可能null = " + list1);

        UserInfo build3 = null;

        List<UserInfo> list2 = Arrays.asList(build0, build1, build2, build3);

        // list 可能有 null ,同时对比的 key 可能null
        final List<UserInfo> list3 = list2.stream()
                .sorted(Comparator.nullsFirst(Comparator.comparing(item -> item.getUid(), Comparator.nullsFirst(Long::compareTo))))
                .collect(Collectors.toList());

        System.out.println("list 可能有 null ,同时对比的 key 可能null = " + list3);
    }

    @Test
    void test_Comparator_key_null() {

        UserInfo build0 = UserInfo.builder().uid(1L).gender(1).build(); // 这个不要uid2
        UserInfo build1 = UserInfo.builder().uid(2L).uid2(2L).gender(0).build();
        UserInfo build2 = UserInfo.builder().uid(3L).uid2(2L).gender(0).build();

        List<UserInfo> list = List.of(build0, build1, build2);

        // 多重排序
        // list 无 null ,但对比的 key 可能null,Comparator.nullsFirst(Comparator.naturalOrder())
        List<UserInfo> list1 = list.stream()
                .sorted(
                        Comparator.comparing(UserInfo::getUid2, Comparator.nullsFirst(Long::compareTo))
                                .reversed() // 这个逆序
                                .thenComparing(UserInfo::getUid) // 这个正序
                )
                .toList();

        System.out.println("key 可能null = " + list1);


        List<UserInfo> list2 = list.stream()
                .sorted(
                        Comparator.comparing(UserInfo::getUid2, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .reversed() // 这个逆序
                                .thenComparing(UserInfo::getUid) // 这个正序
                )
                .toList();

        System.out.println("key 可能null = " + list2);
    }
}
