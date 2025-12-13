package com.github.axinger;

import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ListTest.java
 * @description TODO
 * @createTime 2022年03月22日 17:08:00
 */

public class ListTest {

    @Test
    void test1() {

        // guava 可变
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        list.add(9);
        System.out.println("list = " + list);


        // guava
        // 指定大小分组
        // [[1, 2], [3, 4], [5]]
        final List<List<Integer>> list1 = Lists.partition(list, 2);
        System.out.println("list1 = " + list1);

        // 修改源list,分割数组也会变化, 不能直接修改变化数组
        list.add(10);
        System.out.println("list1 = " + list1);

        // list1.add(List.of(11)); // 报错

        // hutool
        ListUtil.page(list, 3, (val) -> {
            System.out.println("val = " + val);
        });

        // 分页 (2+1)*3
        List page = ListUtil.page(2, 3, list);
        System.out.println("page = " + page);

        List list2 = ListUtil.partition(list, 3);
        System.out.println("list2 = " + list2);
    }

    @Test
    void test3() {

        // guava 可变
        List list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8);
        list.add(9);
        System.out.println("list = " + list);


        // guava
        // 指定大小分组
        // [[1, 2], [3, 4], [5]]
        final List list1 = Lists.partition(list, 2);
        System.out.println("list1 = " + list1);

        // 原list 新增一个
        list.add(10);
        list.add(11);
        // 分割后的list 也会新增,并且也会分组好
        // guava, hutool 原理一样
        System.out.println("list1 = " + list1);


    }


    @Test
    void test2() {

        // guava,可变
        ArrayList<Integer> list1 = Lists.newArrayList(1);
        list1.add(2);
        System.out.println("list1 = " + list1);
    }
}
