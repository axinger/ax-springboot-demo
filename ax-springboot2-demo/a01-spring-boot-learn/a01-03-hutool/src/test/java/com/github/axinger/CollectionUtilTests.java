package com.github.axinger;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName CollectionUtilTests.java
 * @description TODO
 * @createTime 2022年06月13日 09:17:00
 */

public class CollectionUtilTests {

    @Test
    public void test_0() {
//        new ArrayList<>(Collections.singleton("1"));
//
//        Arrays.asList("1").remove(1);

        List<String> listA = ListUtil.of("1", "2", "3", "4", "5");
        List<String> listB = ListUtil.of("1", "2", "3", "7");


        Collection<String> result = ObjectUtil.clone(listA);

        System.out.println("listA = " + System.identityHashCode(listA));
        System.out.println("result = " + System.identityHashCode(listA));


//        result.removeAll(listB);
    }

    @Test
    public void test1() {


//        List<String> listA = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
//
//        List<String> listB = new ArrayList<>(Arrays.asList("1", "2", "3", "7"));

        List<String> listA = ListUtil.toList("1", "2");
        List<String> listB = ListUtil.of("1", "3");


        System.out.println("集合A:" + listA);
        System.out.println("集合B:" + listB);
        System.out.println("------------------------");

        // 并集
        Collection<String> union = CollectionUtil.union(listA, listB);
        System.out.println("并集 union ：" + union);


        // 交集
        Collection<String> intersection = CollectionUtil.intersection(listA, listB);
        System.out.println("交集 intersection :" + intersection);

        // 交集的补集,两个集合的对称差集 (A-B)∪(B-A)
        //  disjunction([a, b, c], [b, c, d])          -》 [a, d]
        Collection<String> disjunction = CollectionUtil.disjunction(listA, listB);
        System.out.println("交集的补集  disjunction ：" + disjunction);

        // 单差集，即只返回【集合1】中有，但是【集合2】中没有的元素
        Collection<String> subtract = CollectionUtil.subtract(listA, listB);
        System.out.println("集合1】中有，但是【集合2】 subtract  ：" + subtract);

    }

    @Test
    public void test1_2() {

        List<Person> build = List.of(
                Person.builder().name("甲").build(),
                Person.builder().name("乙").build(),
                Person.builder().name("丙").build()
        );

        List<Person> build2 = List.of(
                Person.builder().name("丙").build()
        );

        List<String> listA = build.stream().map(Person::getName).toList();
        List<String> listB = build2.stream().map(Person::getName).toList();

        Collection<String> subtract = CollectionUtil.subtract(listA, listB);
        Collection<String> subtract2 = CollectionUtil.subtractToList(listA, listB);
        System.out.println("差集(集合相减) subtract  ：" + subtract);
        System.out.println("subtract2 = " + subtract2);
    }

    @Test
    public void test2() {


        List<String> listA = ListUtil.toList("1", "2", "3", "4", "5");
        List<String> listB = ListUtil.of("1", "2");

        // 交集的补集
        Collection<String> disjunction = CollectionUtil.disjunction(listA, listB);
        System.out.println("交集的补集  disjunction ：" + disjunction);

        Map<String, Object> map = new HashMap<>() {{
            put("1", "1");
            put("3", "3");
        }};

        Collection<String> disjunction2 = CollectionUtil.disjunction(listA, map.keySet());
        System.out.println("disjunction2 = " + disjunction2);
    }

    @Test
    public void test3() {
        List<User> listA = ListUtil.toList(
                User.builder().id(1).name("tom").build(),
                User.builder().id(2).name("tom").build(),
                User.builder().id(3).name("jim").build()
        );

        List<User> listB = ListUtil.toList(
                User.builder().id(1).name("tom").build(),
                User.builder().id(4).name("tom").build(),
                User.builder().id(5).name("jim").build()
        );


        // 并集
        List<User> union = CollectionUtil.union(listA, listB).stream().sorted(Comparator.comparing(User::getId)).toList();
        System.out.println("并集 union ：" + union);


        // 交集
        List<User> intersection = CollectionUtil.intersection(listA, listB).stream().sorted(Comparator.comparing(User::getId)).toList();
        System.out.println("交集 intersection :" + intersection);

        // 交集的补集,不同部分
        List<User> disjunction = CollectionUtil.disjunction(listA, listB).stream().sorted(Comparator.comparing(User::getId)).toList();
        System.out.println("交集的补集  disjunction ：" + disjunction);

        // 差集(集合相减)
        List<User> subtract = CollectionUtil.subtract(listA, listB).stream().sorted(Comparator.comparing(User::getId)).toList();
        System.out.println("差集(集合相减) subtract  ：" + subtract);
    }


    @Test
    public void test4() {


    }
}
