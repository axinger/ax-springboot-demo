package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TreeMapTests {

    @Test
    void test1() {

        //自动排序
//        treeMap.firstEntry()  // 获取最小键的Entry
//        treeMap.lastEntry()   // 获取最大键的Entry
//        treeMap.ceilingEntry() // 获取>=给定键的最小Entry
//        treeMap.floorEntry()   // 获取<=给定键的最大Entry
//        treeMap.headMap()     // 获取所有小于给定键的子Map
//        treeMap.tailMap()     // 获取所有大于等于给定键的子Map
        TreeMap<String, String> treeMap = new TreeMap<>();
        // 插入

        treeMap.put("7", "G");
        treeMap.put("8", "H");
        treeMap.put("9", "I");
        treeMap.put("1", "A");
        treeMap.put("2", "B");
        treeMap.put("3", "C");
        treeMap.put("4", "D");
        treeMap.put("5", "E");
        treeMap.put("6", "F");
        System.out.println("------put()------");
        System.out.println("treeMap: " + treeMap);

        // 访问
        System.out.println("------访问------");
        System.out.println("treeMap.get(1):" + treeMap.get("1"));
        System.out.println("treeMap.get(2):" + treeMap.get("2"));

        System.out.println("treeMap.keySet():" + treeMap.keySet());  // key
        System.out.println("treeMap.entrySet():" + treeMap.entrySet());  // key-value

        System.out.println("treeMap.firstEntry():" + treeMap.firstEntry());  // the first key-value
        System.out.println("treeMap.lastEntry():" + treeMap.lastEntry());  //  the last key-value

        System.out.println("treeMap.ceilingEntry(3):" + treeMap.ceilingEntry("3"));  // 返回大于等于key的key-value
        System.out.println("treeMap.floorEntry(6):" + treeMap.floorEntry("6"));  // 返回小于等于key的key-value

        System.out.println("treeMap.headMap(3):" + treeMap.headMap("3"));  // 返回所有小于key的key-value
        System.out.println("treeMap.tailMap(3):" + treeMap.tailMap("3"));  // 返回所有大于等于key的key-value

        // 判断
        System.out.println("------判断------");
        System.out.println("treeMap.isEmpty():" + treeMap.isEmpty());
        System.out.println("treeMap.containsKey(2):" + treeMap.containsKey("3"));

        // 修改
        System.out.println("------replace()------");
        System.out.println("treeMap.replace(2, 8):" + treeMap.replace("1", "a"));
        System.out.println(treeMap);

        // 遍历
        System.out.println("正序treeMap.descendingMap():" + treeMap);
        System.out.println("倒叙treeMap.descendingMap():" + treeMap.descendingMap());
        System.out.println("------for------");
        for (String k : treeMap.keySet()) {
            System.out.println("key: " + k + ", value: " + treeMap.get(k));
        }

        for (Map.Entry entry : treeMap.entrySet()) {
            System.out.println("key - value: " + entry);
        }
    }


    @Test
    void tet2() {
        // 创建treemap集合对象，并用比较器排序

        TreeMap<Student, String> map = new TreeMap<>((s1, s2) -> {
            int num = s2.getAge() - s1.getAge();
            int num2 = num == 0 ? s1.getName().compareTo(s2.getName()) : num;
            return num2;

        });

        // 添加元素

        Student s1 = new Student("张无忌", 50);

        Student s2 = new Student("赵敏", 45);

        Student s3 = new Student("金毛狮王", 60);

        Student s4 = new Student("周芷若", 40);

        // 把元素添加到集合中去
        map.put(s1, "红楼梦");

        map.put(s2, "西游记");

        map.put(s3, "水浒传");

        map.put(s4, "捉妖记");

        // 获取键的集合

        Set<Student> k = map.keySet();
        // 遍历

        for (Student key : k) {

            // 根据键找值
            String value = map.get(key);

            System.out.println(key.getName() + key.getAge() + "---" + value);

        }

    }
}
