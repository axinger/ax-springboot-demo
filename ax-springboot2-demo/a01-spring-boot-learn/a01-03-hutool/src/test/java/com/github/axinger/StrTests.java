package com.github.axinger;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName StrTests.java
 * @description TODO
 * @createTime 2022年08月02日 17:14:00
 */

public class StrTests {

    /**
     * 对齐
     */
    @Test
    void test_1() {
        System.out.println(StrUtil.padPre("1", 3, "0"));
    }


    @Test
    void test_2() {
        Object obj = "null";
        String string = "null";
        System.out.println("ObjectUtil.equals(obj,string) = " + ObjectUtil.equals(obj, string));
    }

    @Test
    void test_3() {
        List<Integer> list = List.of(1, 2, 3);

        // StrUtil.join(",",list);

        // 每个元素加前后缀 [1],[2],[3] , 不是 [1,2,3]
        String join = CollUtil.join(list, ",", "[", "]");
        System.out.println("join = " + join);

        String format = StrUtil.format("[{}]", CollUtil.join(list, ","));
        System.out.println("format = " + format);

    }
}
