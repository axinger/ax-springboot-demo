package com.github.axinger;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class ArrayUtilsTests {


    //获取元素索引
    @Test
    void test1() {
        char[] arr = {'a', 'b', 'c'};
        int i = ArrayUtils.indexOf(arr, 'b');
        System.out.println("b的索引是：" + i);
//        输出：2的索引是：1
    }

    @Test
    void test2() {

        //数组合并
        int[] arr1 = {1, 2};
        int[] arr2 = {3, 4};
        int[] arr3 = ArrayUtils.addAll(arr1, arr2);
        System.out.println("arr3 = " + Arrays.toString(arr3));
//        输出：{1,2,3,4}
    }


    @Test
    void test3() {
//数组反转

        int[] arr = {1, 2, 3};
        ArrayUtils.reverse(arr);
        System.out.println(ArrayUtils.toString(arr));
//        输出：{3,2,1}
    }

    @Test
    void test4() {
        //数组截取

        int[] arr1 = {1, 2, 3};
//包头不包尾
        int[] arr2 = ArrayUtils.subarray(arr1, 0, 2);
        System.out.println(ArrayUtils.toString(arr2));
//        输出：{1,2}
    }


    @Test
    void test5() {
        //数组排序
        int[] arr = {3, 1, 2};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
//        输出：{1,2,3}
    }

    @Test
    void test6() {
        //数组填充
        int[] arr = new int[3];
        Arrays.fill(arr, 1);
        System.out.println(Arrays.toString(arr));
//        输出：[1, 1, 1]
    }

    @Test
    void test7() {
        //数组复制
        //类似于ArrayUtils.subarray数组截取
        int[] arr1 = {1, 2, 3};
        int[] arr2 = Arrays.copyOfRange(arr1, 0, 2);
        System.out.println(Arrays.toString(arr2));
//        输出：[1, 2]
    }

    @Test
    void test8() {
        //数组比较

        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 4};
        System.out.println(Arrays.equals(arr1, arr2));
//        输出：false
    }

    @Test
    void test9() {

        List<Integer> sortedNumbers = Arrays.asList(1, 2, 3, 5, 8);
        // 二分查找
        int index = Collections.binarySearch(sortedNumbers, 3);
        System.out.println("数字3的索引: " + index);
        //        输出：数字3的索引: 2
        // 查找不存在的元素
        int notFoundIndex = Collections.binarySearch(sortedNumbers, 4);
        System.out.println("数字4的索引（负数表示未找到）: " + notFoundIndex);//输出：数字4的索引（负数表示未找到）: -5
    }


    @Test
    void test10() {
        //线程安全处理,将非同步集合包装成同步集合，确保线程安全

        List<String> list = new ArrayList<>();
        // 创建同步列表
        List<String> synchronizedList = Collections.synchronizedList(list);
        // 同步块内操作集合
        synchronized (synchronizedList) {
            synchronizedList.add("Hello");
            synchronizedList.add("World");
        }
    }

    @Test
    void test11() {
        //将现有集合转换为不可变集合

        // 创建不可变空列表
        List<String> emptyList = Collections.emptyList();
        System.out.println("不可变空列表: " + emptyList);
//        输出：不可变空列表: []
// 创建不可变单元素列表
        List<String> singletonList = Collections.singletonList("Only One");
        System.out.println("不可变单元素列表: " + singletonList);
//        输出：不可变单元素列表: [Only One]
// 将可变列表转换为不可变列表
        List<String> mutableList = new ArrayList<>(Arrays.asList("a", "b", "c"));
        List<String> unmodifiableList = Collections.unmodifiableList(mutableList);
        System.out.println("不可变列表: " + unmodifiableList);
//        输出：不可变列表: [a, b, c]
// 尝试修改不可变列表将抛出UnsupportedOperationException
// unmodifiableList.add("d"); // Uncommenting this line will throw an exceptio
    }


    @Test
    void test12() {

        //统计list中某元素出现的次数
        List<String> list = Arrays.asList("1", "2", "3", "1", "1", "2");
        int count = Collections.frequency(list, "1");
        System.out.println(count);
//        输出：3

    }

    @Test
    void test13() {
        List<String> list = Arrays.asList("1", "2", "3", "1", "1", "2");
        Map<String, Long> frequencyMap = list.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        for (Map.Entry<String, Long> entry : frequencyMap.entrySet()) {
            System.out.println("元素" + entry.getKey() + "出现次数：" + entry.getValue());
        }
    }

    @Test
    void test14() {

//        List<String> list = Arrays.asList("1", "2", "3", "1", "1", "2");
//        Bag bag = new HashBag(list);
//        System.out.println("元素出现次数：" + bag.getCount("1"));
//        输出：元素出现次数：3
    }
}
