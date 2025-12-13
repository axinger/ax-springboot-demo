package com.github.axinger.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// 归约 减少 ，也称缩减，顾名思义，是把一个流缩减成一个值，能实现对集合求和、求乘积和求最值操作。
public class StreamTest_reduce {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(0, 1, 3, 2, 8, 11, 4);
        // 求和方式1
        Optional<Integer> sum = list.stream().reduce((x, y) -> x + y);
        // 求和方式2
        Optional<Integer> sum2 = list.stream().reduce(Integer::sum);
        // 求和方式3
        Integer sum3 = list.stream().reduce(0, Integer::sum);

        final Integer sum4 = list.stream().collect(Collectors.summingInt(value -> value));

        System.out.println("list求和：" + sum.get() + "," + sum2.get() + "," + sum3 + " Collectors方式求和 sum4 = " + sum4);


        // 求乘积
        Optional<Integer> product = list.stream().reduce((x, y) -> x * y);

        System.out.println("list求积：" + product.get());

        // 求最大值方式1
        Optional<Integer> max = list.stream().reduce((x, y) -> x > y ? x : y);

        // Stream的错误使用：Stream.max(Integer::max)和Stream.min(Integer::min)
        Optional<Integer> max1 = list.stream().max(Integer::max);

//        Optional<Integer> max2 = list.stream().max(Integer::compare);
        Optional<Integer> max2 = list.stream().max(Comparator.naturalOrder());

        System.out.println("降序,查找最小 = " + list.stream().max(Comparator.reverseOrder()));

        // 求最大值写法2
        Integer max3 = list.stream().reduce(1, Integer::max);

        System.out.println("最大值：" + max.get() + ",max1错误用法 = " + max1.get() + ",max2 = " + max2 + ",max3 = " + max3);


        List<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5, 6);


        System.out.println("最大值 max 正确用法,所以需要用排序方法：" + list1.stream().max((a, b) -> {
            if (a > b) {
                return 1;
            } else return -1;
        }).get());


        // 其实result返回的一直是两个值的最大值，而数据中全部都为正数，所以返回值总是正数。
        int a = Stream.of(2, 1, 4, 5, 3).max((v, k) -> {
            System.out.println("v = " + v);
            System.out.println("k = " + k);
            int result = Integer.max(v, k);
            System.out.println("result = " + result);
            return result;
        }).get();
        System.out.println(a);


    }
}
