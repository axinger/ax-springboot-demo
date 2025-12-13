package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionTest {
    @Test
    public void test1() {
        List<Integer> list = Arrays.asList(1, 23);
    }

    @Test
    public void test3() {
        ArrayList list = new ArrayList<Person>() {
            {
                Person.builder()
                        .id(1)
                        .build();
            }
        };

        Person p2 = Person.builder()
                .id(2)
                .build();

        list.add(p2);


        System.out.println("list = " + list);


        /// 注意点
        ArrayList copyList = new ArrayList(Arrays.asList(new Object[list.size()]));
//        List copyList = Arrays.asList(new Object[list.size()]);
//        ArrayList copyList = new ArrayList<Person>();
        Collections.copy(copyList, list);
        System.out.println("copyList = " + copyList);

        /// 返回线程安全的集合
        List synchronizedList = Collections.synchronizedList(copyList);
    }


    @Test
    void test_map() {

        Map<String, Object> map = new HashMap<>();

        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        System.out.println("map.entrySet() = " + map.entrySet());

        map.entrySet().parallelStream().forEach(val -> {

            System.out.println("val.getKey() = " + val.getKey());
            System.out.println("val.getValue() = " + val.getValue());
        });

    }

    @Test
    public void test10() {

        /***
         * Collection 子接口 List
         *
         * 1.ArrayList 主要实现类
         * 2.LinkedList
         * 3.Vector 古老实现类
         */
        List arrayList = new ArrayList();
        LinkedList<Object> linkedList = new LinkedList<>();
        Vector<Object> vector = new Vector<>();
        arrayList.add(1);
        arrayList.add(2);
//        arrayList.remove(2);//remove 是index,
//        arrayList.remove(new Integer(2));

        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            System.out.println("iterator.next() = " + iterator.next());
        }

    }

    @Test
    public void test2() {
        HashSet<Object> hashSet = new HashSet<>();
        hashSet.add(123);
        hashSet.add("a");
        hashSet.add(Person.builder()
                .id(1)
                .build());
        hashSet.add(Person.builder()
                .id(2)
                .build());

        hashSet.forEach(e -> {
            System.out.println("e = " + e);
        });
//        hashSet.stream().forEach(e->{
//            System.out.println("e = " + e);
//        });
        new LinkedHashSet<>();


        /// 相同对象, 判断重复,根据 compareTo 进行判断
        TreeSet<Object> treeSet = new TreeSet<>();
//        treeSet.add(new Integer(1));
        treeSet.add(Person.builder()
                .id(1)
                .build());
        treeSet.add(Person.builder()
                .id(1)
                .build());
        treeSet.add(Person.builder()
                .id(1)
                .build());
        System.out.println("treeSet = " + treeSet);
        treeSet.forEach(e -> {
            System.out.println("e = " + e);
        });

    }


    @Test
    void test11() {
        Set<String> set1 = new HashSet<>(List.of("001", "002", "004"));

        Set<String> set2 = new HashSet<>(List.of("002", "004"));

        /// 交集
        set1.retainAll(set2);

        System.out.println("交集 = " + set1);

    }

    @Test
    void test21() {
        Set<String> set1 = new HashSet<>(List.of("001", "002", "004"));
        Set<String> set2 = new HashSet<>(List.of("002", "004", "005"));


        // 并集
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("并集 = " + union);

        // 交集
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("交集 = " + intersection);

        // 差集
        Set<String> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("差集 = " + difference);


        // 计算 set1 有但 set2 没有的元素
        Set<String> diff1 = new HashSet<>(set1);
        diff1.removeAll(set2); // 结果: ["001"]
        System.out.println("计算 set1 有但 set2 没有的元素 = " + diff1);

        Set<String> result = Stream.concat(set1.stream(), set2.stream())
                .filter(e -> !(set1.contains(e) && set2.contains(e)))
                .collect(Collectors.toSet());
        System.out.println("互相不存在" + result); // 输出: [001, 005]


        // 判断子集
        boolean isSubset = set1.containsAll(set2);
        System.out.println("set1 是否包含 set2 的所有元素 = " + isSubset);

    }

    @Test
    void test31() {
        // 方式1: 直接对比两个集合差异
        List<String> list0 = getList(0); // ["a","b"]
        List<String> list1 = getList(1); // ["a","c"]

        // 找出互相不存在的元素（差集）
        List<String> difference = Stream.concat(
                list0.stream().filter(e -> !list1.contains(e)),
                list1.stream().filter(e -> !list0.contains(e))
        ).collect(Collectors.toList());

        System.out.println("差异元素: " + difference); // 输出 [b, c]

        // 方式2: 分组统计差异（更直观）
        Map<Boolean, List<String>> partitioned = Stream.concat(list0.stream(), list1.stream())
                .distinct()
                .collect(Collectors.partitioningBy(e -> list0.contains(e) && list1.contains(e)));

        List<String> commonElements = partitioned.get(true);  // 交集 ["a"]
        List<String> uniqueElements = partitioned.get(false); // 差集 ["b","c"]

        System.out.println("commonElements = " + commonElements);
        System.out.println("uniqueElements = " + uniqueElements);
    }


    List<String> getList(int i) {
        if (i == 0) {
            return List.of("a", "b");
        } else if (i == 1) {
            return List.of("a", "c");
        }
        return null;
    }

}
