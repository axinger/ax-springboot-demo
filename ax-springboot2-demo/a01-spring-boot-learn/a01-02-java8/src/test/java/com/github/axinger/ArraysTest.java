package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/***
 * Arrays 数组工具, String[] 这样的才是数组 list是集合
 *
 */
public class ArraysTest {

    /// 数组转字符串
    @Test
    public void test1() {

        int[] array = new int[]{1, 2, 3};
        System.out.println("array = " + array);
        System.out.println("Arrays.toString(array) = " + Arrays.toString(array));
    }

    /// 但遇到多维数组时，
    @Test
    public void test2() {
        int[][] deepArray = new int[][]{{1, 3}, {2, 4}};
        System.out.println(Arrays.toString(deepArray)); //[[I@1540e19d, [I@677327b6]
        System.out.println(Arrays.deepToString(deepArray)); //[[1, 3], [2, 4]]
    }

    @Test
    public void test3() {
        List<Person> list = new ArrayList<>() {
            {
                add(Person.builder()
                        .id(1)
                        .build());
            }
        };

        Person p2 = Person.builder().id(2).build();

        list.add(p2);

        System.out.println("list = " + list);

    }


    @Test
    public void test4() {
        List<? extends Person> list1 = null;
        List<? super Person> list2 = null;


        List<Student> list3 = null;
        List<Person> list4 = null;
        List<Object> list5 = null;
        list1 = list3;
        list1 = list4;
//        list1 = list5; // 这个不行

//        list2 = list3; // 这个不行
        list2 = list4;
        list2 = list5;


    }

    @Test
    public void test5() {
        /// Arrays.asList 不可变
        List<Integer> list = Arrays.asList(1, 2, 3);
        list.add(4);
        System.out.println("list = " + list);
    }

    @Test
    public void test6() {
        int[] numbers = {1, 2, 3, 4, 5};
        // 计算前缀和
        Arrays.parallelPrefix(numbers, (left, right) -> left + right);
        System.out.println(Arrays.toString(numbers));
        // 输出: [1, 3, 6, 10, 15]

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Integer[] array = list.toArray(new Integer[0]);
        Arrays.parallelPrefix(array, (left, right) -> left + right);
        System.out.println("list = " + list);
        System.out.println("array = " + Arrays.toString(array));
    }

    @Test
    public void test7() {
        int[] numbers = {1, 2, 3, 4, 5};
        Arrays.parallelPrefix(numbers, (a, b) -> a * b);
        System.out.println(Arrays.toString(numbers));
        // 输出: [1, 2, 6, 24, 120]
    }

    @Test
    public void test8() {
        String[] strings = {"a", "b", "c", "d"};
        Arrays.parallelPrefix(strings, (s1, s2) -> s1 + s2);
        System.out.println(Arrays.toString(strings));
        // 输出: [a, ab, abc, abcd]
    }

    @Test
    public void test9() {
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8};
        // 只计算索引2到6的前缀和(包含2，不包含6)
        Arrays.parallelPrefix(numbers, 2, 6, (a, b) -> a + b);
        System.out.println(Arrays.toString(numbers));
        // 输出: [1, 2, 3, 7, 12, 18, 7, 8]
    }


}
