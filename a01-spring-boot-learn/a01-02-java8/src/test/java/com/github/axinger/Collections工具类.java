package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.*;

public class Collections工具类 {

    @Test
    public void test1() {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);

        // 对List进行自然排序（元素必须实现Comparable接口）
        Collections.sort(list);
        System.out.println("自然排序 = " + list);

        // 使用自定义Comparator排序
        Collections.sort(list, Comparator.reverseOrder());
        System.out.println("用自定义Comparator排序 = " + list);

        list.sort(Comparator.reverseOrder());
        System.out.println("用自定义Comparator排序 = " + list);

        // 反转List中元素的顺序
        Collections.reverse(list);
        System.out.println("反转List中元素的顺序 = " + list);

        // 随机打乱List中元素的顺序
        Collections.shuffle(list);
        System.out.println("随机打乱List中元素的顺序 = " + list);

        // 交换List中两个位置的元素
        Collections.swap(list, 0, 2);
        System.out.println("交换List中两个位置的元素 = " + list);

        // 旋转List中的元素（将后distance个元素移到前面）
        Collections.rotate(list, 2);
        System.out.println("旋转List中的元素 = " + list);

    }

    @Test
    public void test2() {

        List<String> list = Arrays.asList("A", "B", "C", "D");
        List<String> list2 = Arrays.asList("A", "B");

        // 二分查找（List必须已排序）
        int index = Collections.binarySearch(list, "C");
        System.out.println("二分查找= " + index);


// 使用自定义Comparator的二分查找
        int index2 = Collections.binarySearch(list, "C", Comparator.reverseOrder());
        System.out.println("使用自定义Comparator的二分查找= " + index2);

// 返回集合中的最大值（自然顺序）
        Object max = Collections.max(list);
        System.out.println("返回集合中的最大值（自然顺序）= " + max);

// 使用自定义Comparator返回最大值
        Object max2 = Collections.max(list, Comparator.reverseOrder());
        System.out.println("使用自定义Comparator返回最大值= " + max2);

// 返回集合中的最小值（自然顺序）
        Object min = Collections.min(list);
        System.out.println("返回集合中的最小值（自然顺序）= " + min);

// 使用自定义Comparator返回最小值
        Object min2 = Collections.min(list, Comparator.nullsFirst(Comparator.reverseOrder()));
        System.out.println("使用自定义Comparator返回最小值= " + min2);

// 返回指定对象在集合中出现的次数
        int freq = Collections.frequency(list, "C");
        System.out.println("返回指定对象在集合中出现的次数= " + freq);

// 检查两个集合是否有交集（至少一个共同元素）
        boolean disjoint = Collections.disjoint(list, list2);
        System.out.println("检查两个集合是否有交集（至少一个共同元素）= " + disjoint);
    }

    @Test
    public void test3() {
        // 返回不可变的空集合
//        Collections.emptyList();
//        Collections.emptySet();
//        Collections.emptyMap();
//
//// 返回只包含指定元素的不可变集合
//        Collections.singleton(Object o);
//        Collections.singletonList(Object o);
//        Collections.singletonMap(Object key, Object value);
//
//// 返回指定集合的不可变视图
//        Collections.unmodifiableCollection(Collection c);
//        Collections.unmodifiableList(List list);
//        Collections.unmodifiableSet(Set s);
//        Collections.unmodifiableMap(Map m);

        // 返回集合的同步（线程安全）版本
//        Collections.synchronizedCollection(Collection c);
//        Collections.synchronizedList(List list);
//        Collections.synchronizedSet(Set s);
//        Collections.synchronizedMap(Map m);
    }

    @Test
    public void test4() {
        List<String> list = Arrays.asList("A", "B", "C", "D");
//        List<String> list = new ArrayList<>(5);
        // 用指定对象替换List中的所有元素
        Collections.fill(list, "F");
        System.out.println("用指定对象替换List中的所有元素= " + list);

// 用新值替换List中所有的旧值
        Collections.replaceAll(list, "B", "C");
        System.out.println("用新值替换List中所有的旧值= " + list);


        // 复制源List到目标List（目标List必须足够大）
        List<String> list2 = new ArrayList<>(10);
        Collections.copy(list, list2);
        System.out.println("list2 = " + list2);

// 反转Comparator的顺序
//        Comparator reversed = Collections.reverseOrder();
//        Comparator reversedCustom = Collections.reverseOrder(Comparator cmp);

// 为Enumeration添加迭代器功能
//        Collections.list(list);

// 返回包含n个指定对象的不可变列表
        List<String> nCopies = Collections.nCopies(3, "G");
        System.out.println("返回包含n个指定对象的不可变列表= " + nCopies);

// 使用指定值初始化Map的所有值（如果键不存在）
        List<String> list3 = new ArrayList<>(10);
        Collections.addAll(list3, "A", "B");
        System.out.println("list3 = " + list3);

    }


    @Test
    public void test5() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(1L);
        userInfo.setUid2(2L);
        userInfo.setGender(1);
        Map<String, Object> context = Collections.singletonMap("user", userInfo);
        System.out.println("context = " + context);
    }
}
