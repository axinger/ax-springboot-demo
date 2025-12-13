package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class MapTest {

    @Test
    public void tes1() throws IOException {


        HashMap<Object, Object> hashMap = new HashMap<>();//
        hashMap.put("d", "d");
        hashMap.put("b", "b");
        hashMap.put("a", "a");
        hashMap.forEach((key, val) -> {
//            System.out.println("hashMap key = " + key +" val = " +val );
            System.out.println(" val = " + val);
        });

        hashMap.entrySet().stream().forEach((val) -> {
            System.out.println(" stream val = " + val);
        });


        //默认值
        hashMap.getOrDefault("name", "test");

        new LinkedHashMap<>();

        TreeMap<Object, Object> treeMap = new TreeMap<>();
        treeMap.put("d", "d");
        treeMap.put("b", "b");
        treeMap.put("a", "a");
        treeMap.forEach((key, val) -> {
            System.out.println("treeMap key = " + key + " val = " + val);
        });

        // Properties extends Hashtable
        Properties properties = new Properties();
        FileInputStream stream = new FileInputStream("src/jdbc.properties");
        properties.load(stream);
        System.out.println("properties = " + properties);


    }

    @Test
    public void tes2() {
        ///  可以
        Map<Object, Object> map = new HashMap<>();
        map.put("name", null);
        map.put("NAME", "大D");
        System.out.println("map = " + map);
        ///  可以
        map.put(null, null);
        System.out.println("map = " + map);
    }

    @Test
    public void tes3() {
        //不可用,ImmutableCollections 不能添加null
        Map<Object, Object> map = Map.of("a", null);
        System.out.println("map = " + map);
    }

    @Test
    public void tes4() {
        ///  可以
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");


        /// 不能删除
        map.forEach((key, val) -> {

            if (key.equals("a")) {
                map.remove(key);
            }
        });
        System.out.println("map = " + map);
    }

    @Test
    @SuppressWarnings("all")
    public void tes5() {
        ///  可以
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            if (entry.getKey().equals("a")) {
                iterator.remove(); // ✅ 通过迭代器安全删除
            }
        }
        System.out.println("map = " + map); // 输出 {b=2, c=3}
    }

    @Test
    public void tes6() {
        ///  可以
        Map<Object, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");

        map.entrySet().removeIf(entry -> entry.getKey().equals("a"));
        System.out.println("map = " + map); // 输出 {b=2, c=3}
    }
}
