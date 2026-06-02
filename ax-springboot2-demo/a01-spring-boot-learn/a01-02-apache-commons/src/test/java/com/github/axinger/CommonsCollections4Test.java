package com.github.axinger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.Test;

import java.util.*;

class CommonsCollections4Test {

    // ==================== CollectionUtils ====================

    @Test
    void testCollectionUtils_empty() {
        List<String> nullList = null;
        List<String> emptyList = new ArrayList<>();
        List<String> list = Arrays.asList("a", "b");

        // 判空（null 或 empty 都返回 true）
        System.out.println(CollectionUtils.isEmpty(nullList));   // true
        System.out.println(CollectionUtils.isEmpty(emptyList));  // true
        System.out.println(CollectionUtils.isEmpty(list));       // false

        // 判非空
        System.out.println(CollectionUtils.isNotEmpty(list));    // true
    }

    @Test
    void testCollectionUtils_union() {
        List<String> list1 = Arrays.asList("a", "b", "c");
        List<String> list2 = Arrays.asList("b", "c", "d");

        // 并集
        Collection<String> union = CollectionUtils.union(list1, list2);
        System.out.println("并集: " + union); // [a, b, c, d]

        // 交集
        Collection<String> intersection = CollectionUtils.intersection(list1, list2);
        System.out.println("交集: " + intersection); // [b, c]

        // 差集 (list1 - list2)
        Collection<String> subtract = CollectionUtils.subtract(list1, list2);
        System.out.println("差集: " + subtract); // [a]

        // 对称差集
        Collection<String> disjunction = CollectionUtils.disjunction(list1, list2);
        System.out.println("对称差集: " + disjunction); // [a, d]
    }

    @Test
    void testCollectionUtils_filter() {
        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));

        // 过滤偶数（保留符合条件的）
        CollectionUtils.filter(numbers, n -> n % 2 == 0);
        System.out.println("偶数: " + numbers); // [2, 4, 6]
    }

    @Test
    void testCollectionUtils_countMatches() {
        List<String> list = Arrays.asList("apple", "banana", "apricot", "cherry");

        // 统计匹配数量
        long count = CollectionUtils.countMatches(list, s -> s.startsWith("a"));
        System.out.println("以 a 开头的数量: " + count); // 2

        // 是否存在匹配
        boolean any = CollectionUtils.exists(list, s -> s.length() > 6);
        System.out.println("存在长度>6的: " + any); // true
    }

    @Test
    void testCollectionUtils_transform() {
        List<String> list = Arrays.asList("a", "b", "c");

        // 转换集合元素
        Collection<String> upper = CollectionUtils.collect(list, String::toUpperCase);
        System.out.println("大写: " + upper); // [A, B, C]
    }

    // ==================== ListUtils ====================

    @Test
    void testListUtils() {
        List<String> list1 = Arrays.asList("a", "b");
        List<String> list2 = Arrays.asList("c", "d");

        // 合并列表
        List<String> union = ListUtils.union(list1, list2);
        System.out.println("合并: " + union); // [a, b, c, d]

        // 交集
        List<String> intersection = ListUtils.intersection(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("b", "c", "d")
        );
        System.out.println("交集: " + intersection); // [b, c]

        // 差集
        List<String> subtract = ListUtils.subtract(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("b", "c")
        );
        System.out.println("差集: " + subtract); // [a]

        // 空列表（null 安全）
        List<String> nullSafe = ListUtils.emptyIfNull(null);
        System.out.println("空列表大小: " + nullSafe.size()); // 0
    }

    // ==================== MapUtils ====================

    @Test
    void testMapUtils() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        // 安全获取（key 不存在返回 null，不会抛异常）
        System.out.println(MapUtils.getInteger(map, "a"));      // 1
        System.out.println(MapUtils.getInteger(map, "c"));      // null

        // 带默认值
        System.out.println(MapUtils.getInteger(map, "c", 0));   // 0
        System.out.println(MapUtils.getString(map, "a", "default")); // 1 的字符串形式

        // 空 Map 处理
        Map<String, String> emptyMap = null;
        System.out.println(MapUtils.isEmpty(emptyMap));         // true
        System.out.println(MapUtils.isEmpty(map));              // false

        // 空 Map 替代
        Map<String, String> safeMap = MapUtils.emptyIfNull(emptyMap);
        System.out.println(safeMap.size()); // 0
    }

    // ==================== SetUtils ====================

    @Test
    void testSetUtils() {
        Set<String> set1 = new HashSet<>(Arrays.asList("a", "b", "c"));
        Set<String> set2 = new HashSet<>(Arrays.asList("b", "c", "d"));

        // 并集
        Set<String> union = SetUtils.union(set1, set2);
        System.out.println("并集: " + union); // [a, b, c, d]

        // 交集
        Set<String> intersection = SetUtils.intersection(set1, set2);
        System.out.println("交集: " + intersection); // [b, c]

        // 差集
        Set<String> difference = SetUtils.difference(set1, set2);
        System.out.println("差集: " + difference); // [a]

        // 空 Set
        Set<String> empty = SetUtils.emptySet();
        System.out.println("空Set: " + empty);
    }

    // ==================== Bag（多重集合）====================

    @Test
    void testBag() {
        // Bag 允许元素重复，记录每个元素出现的次数
        HashBag<String> bag = new HashBag<>();
        bag.add("apple", 3);
        bag.add("banana", 2);
        bag.add("apple", 2); // apple 现在出现 5 次

        System.out.println("apple 数量: " + bag.getCount("apple")); // 5
        System.out.println("banana 数量: " + bag.getCount("banana")); // 2
        System.out.println("总元素数: " + bag.size()); // 7
        System.out.println("唯一元素数: " + bag.uniqueSet().size()); // 2

        // 移除
        bag.remove("apple", 2);
        System.out.println("移除后 apple 数量: " + bag.getCount("apple")); // 3
    }

    // ==================== MultiMap ====================

    @Test
    void testMultiMap() {
        // 一个 key 可以对应多个 value
        ArrayListValuedHashMap<String, String> multiMap = new ArrayListValuedHashMap<>();
        multiMap.put("fruits", "apple");
        multiMap.put("fruits", "banana");
        multiMap.put("fruits", "orange");
        multiMap.put("vegetables", "carrot");

        List<String> fruits = multiMap.get("fruits");
        System.out.println("水果: " + fruits); // [apple, banana, orange]

        System.out.println("所有 keys: " + multiMap.keySet());
        System.out.println("所有 values: " + multiMap.values());
    }

    // ==================== CaseInsensitiveMap ====================

    @Test
    void testCaseInsensitiveMap() {
        // 大小写不敏感的 Map
        CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
        map.put("Name", "Alice");
        map.put("AGE", "25");

        System.out.println(map.get("name"));    // Alice
        System.out.println(map.get("NAME"));    // Alice
        System.out.println(map.get("age"));     // 25
        System.out.println(map.get("Age"));     // 25
    }

    // ==================== LRUMap ====================

    @Test
    void testLRUMap() {
        // LRU (Least Recently Used) 缓存，容量满时淘汰最久未使用的
        LRUMap<String, String> lruMap = new LRUMap<>(3);
        lruMap.put("a", "A");
        lruMap.put("b", "B");
        lruMap.put("c", "C");
        System.out.println("初始: " + lruMap); // {a=A, b=B, c=C}

        // 访问 a，使其变为最近使用
        lruMap.get("a");

        // 添加 d，容量已满，淘汰最久未使用的 b
        lruMap.put("d", "D");
        System.out.println("添加d后: " + lruMap); // {a=A, c=C, d=D}
    }
}
