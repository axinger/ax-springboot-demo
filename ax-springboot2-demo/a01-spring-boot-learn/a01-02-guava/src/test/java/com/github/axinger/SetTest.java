package com.github.axinger;

import com.google.common.collect.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class SetTest {


    @Test
    void set() {
        HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
        HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);

        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println("union:" + union);

        Sets.SetView<Integer> difference = Sets.difference(setA, setB);
        System.out.println("difference:" + difference);

        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        System.out.println("intersection:" + intersection);

    }


    @Test
    void set_2() {
        HashSet<Integer> setA = Sets.newHashSet(1, 2, 3, 4, 5);
        HashSet<Integer> setB = Sets.newHashSet(5);


        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println("union:" + union);

        Sets.SetView<Integer> difference = Sets.difference(setA, setB);
        System.out.println("difference:" + difference);

        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        System.out.println("intersection:" + intersection);

    }

    // 双键 map - 超级实用
    @Test
    void test_HashBasedTable() {
        Table<String, String, Object> tables = HashBasedTable.create();
        tables.put("财务部", "总监", "a");
        tables.put("财务部", "职员", "b");
        tables.put("法务部", "助理", "c");
        System.out.println("双键 map = " + tables);
        System.out.println("双键 map 取值 = " + tables.get("财务部", "总监"));
    }


    @Test
    void difference_set() {
        final HashSet<Integer> set1 = Sets.newHashSet(1, 2, 3, 10);
        final HashSet<Integer> set2 = Sets.newHashSet(4, 5, 10);


        // 合集，并集   并集 = [1, 2, 10, 3, 4, 5]
        Set<Integer> result1 = Sets.union(set1, set2);
        System.out.println("并集 = " + result1);

        // 交集         交集 = [10]
        Set<Integer> result2 = Sets.intersection(set1, set2);
        System.out.println("交集 = " + result2);

        // 差集 1中有而2中没有的 差集,左独有 = [1, 2, 3]
        Set<Integer> result3 = Sets.difference(set1, set2);
        System.out.println("差集,set1独有 = " + result3);

        Set<Integer> result3_2 = Sets.difference(set2, set1);
        System.out.println("差集,set2独有 = " + result3_2);

        // 外集 ,去除相同的 外集 = [1, 2, 3, 4, 5]
        Set<Integer> result4 = Sets.symmetricDifference(set1, set2);
        System.out.println("外集 = " + result4);

    }


    // map的一个key可以关联多个value
    @Test
    void test2() {
        ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("name", "jim");
        multimap.put("name", "tom");
        multimap.put("name", "jack");

        System.out.println("multimap = " + multimap);
        System.out.println("multimap.get(\"name\") = " + multimap.get("name"));


    }

    // map通过value获取key
    @Test
    void test_HashBiMap() {
        HashBiMap map = HashBiMap.create();
        map.put("name", "jim");
        System.out.println("map.get(\"name\") = " + map.get("name"));
        System.out.println("map.inverse().get(\"jim\") = " + map.inverse().get("jim"));
    }

    // 复制一个map，新的map的值不可变,ImmutableMap 不可变map
    @Test
    void test_ImmutableMap() {


        final ImmutableMap<Object, Object> map3 = ImmutableMap.of("name", "jim", "age", 20);
        System.out.println("不可变map = " + map3);


        final ImmutableMap<Object, Object> map2 = ImmutableMap.builder()
                .put("name", "tom")
                .put("age", 20)
                .build();
//        map2 .put("name", "tom");
        System.out.println("不可变map = " + map2);

        Map map = new HashMap();
        map.put("name", "jim");

        Map map1 = ImmutableMap.copyOf(map);
        map.put("age", 20);

        System.out.println("map = " + map);
        System.out.println("map1 = " + map1);


    }
}
