package com.github.axinger;

import com.google.common.collect.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTests {

    @Test
    void test() {

        HashMap<Object, Object> map = Maps.newHashMap(new HashMap<>() {{
            put("a", 1);
        }});
        System.out.println("map = " + map);

        Map<Object, Object> map1 = new HashMap<>() {{
            put("a", 1);
        }};
        System.out.println(map1);
    }


    @Test
    void map_filterKeys() {
        Map<Object, Object> map1 = Maps.newHashMap();
        map1.put("name", "jim");
        map1.put("age", 20);
        // map过滤
        final Map<Object, Object> filterKeys = Maps.filterKeys(map1, key -> key.equals("name"));
        System.out.println("filterKeys = " + filterKeys);
    }

    @Test
    void map() {
        Map<Object, Object> map1 = Maps.newHashMap();
        map1.put("name", "jim");
        map1.put("age", 20);

        Map<Object, Object> map2 = Maps.newHashMap();
        map2.put("name", "tom");
        map2.put("high", 20);
        // 交集
        MapDifference<Object, Object> difference = Maps.difference(map1, map2);

        System.out.println("求两个map集合的差集,key相同,value不同 = " + difference);

        // 左边差集
        final Map<Object, Object> onlyOnLeft = difference.entriesOnlyOnLeft();
        System.out.println("map1 独有 = " + onlyOnLeft);
        // 右边差集
        final Map<Object, Object> onlyOnRight = difference.entriesOnlyOnRight();
        System.out.println("map2 独有 = " + onlyOnRight);
        System.out.println("difference.entriesDiffering() = " + difference.entriesDiffering());
        System.out.println("difference.entriesInCommon() = " + difference.entriesInCommon());
    }

    @Test
    void test2() {
        // map元素为list,Key 可以重复
        Multimap<String, Object> map = ArrayListMultimap.create();
        map.put("name", "jim");
        map.put("name", "tom");

        map.put("age", "1");
        map.put("age", "2");

        // map = {name=[jim, tom], age=[1, 2]}
        System.out.println("map = " + map);

        // map.entries().stream().forEach((val->{
        //
        //     // System.out.println("val.getClass() = " + val.getClass());
        //     // System.out.println("val.getValue().getClass() = " + val.getValue().getClass());
        //
        //     System.out.println("val.getKey() = " + val.getKey());
        //     System.out.println("val.getValue() = " + val.getValue());
        // }));
        //
        map.keys().stream().forEach(val -> {

            System.out.println("val = " + val);
        });
        map.isEmpty();
        System.out.println("map.keys() = " + map.keys().stream().distinct().toList());

        System.out.println("map.get(\"name\") = " + map.get("name"));
    }

    @Test
    void test_双keymap() {
        Table<String, String, List<Object>> tables = HashBasedTable.create();
        tables.put("阿里", "java", Lists.newArrayList(1));
        tables.put("腾讯", "java", Lists.newArrayList(2));
        tables.put("腾讯", "python", Lists.newArrayList(3));

        List<Object> tableResult = tables.get("腾讯", "java");


        tables.cellSet().stream().forEach((k1) -> {


            System.out.println("k1.getRowKey() = " + k1.getRowKey());
            System.out.println("k1.getColumnKey() = " + k1.getColumnKey());
            System.out.println("k1.getValue() = " + k1.getValue());

            System.out.println("tables.get( k1.getRowKey(),k1.getColumnKey()) = " + tables.get(k1.getRowKey(), k1.getColumnKey()));
            k1.getValue().add(10);
        });


        for (Object o : tableResult) {
            System.out.println("table-----" + o);
        }
    }


}
