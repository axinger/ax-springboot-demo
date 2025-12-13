package com.github.axinger;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * Objects jdk工具类
 */
@SuppressWarnings("all")
public class 工具类Test {

    @Test
    @SuppressWarnings("all")
    void test() {
        String str = "abc";
        String str1 = Objects.requireNonNull(str);
        System.out.println("str1 = " + str1);


        str = null;

        /// 默认值
        String jim = Objects.requireNonNullElse(str, "jim");
        System.out.println("jim = " + jim);
//        String str2 = Objects.requireNonNull(str);
        ///  null 抛异常
        String str2 = Objects.requireNonNull(str, "不能为空");
        System.out.println("str2 = " + str2);
    }

    @Test
    void test2() {
        String str = null;

        /// 默认值
        boolean b = Objects.nonNull(str);
        System.out.println("b = " + b);
        boolean b1 = Objects.isNull(str);
        System.out.println("b1 = " + b1);
    }

    @Test
    void test3() {
        String str = null;
        boolean empty = StringUtils.isEmpty(str);
        boolean noneEmpty = StringUtils.isNoneEmpty(str);
        boolean notBlank = StringUtils.isNotBlank(str);
        boolean notEmpty = StringUtils.isNotEmpty(str);
        boolean alpha = StringUtils.isAlpha(str);
        boolean noneBlank = StringUtils.isNoneBlank(str);
        System.out.println("empty = " + empty);
        System.out.println("notEmpty = " + notEmpty);
        System.out.println("notBlank = " + notBlank);
        System.out.println("alpha = " + alpha);
        System.out.println("noneBlank = " + noneBlank);
        System.out.println("noneEmpty = " + noneEmpty);

        boolean anyEmpty = StringUtils.isAnyEmpty(str, "a", "b", "c");
        System.out.println("anyEmpty = " + anyEmpty);
        boolean noneEmpty1 = StringUtils.isNoneEmpty(str, "a", "b", "c");
        System.out.println("noneEmpty1 = " + noneEmpty1);

        boolean allEmpty = StringUtils.isAllEmpty(str, "a", "b", "c");
        System.out.println("allEmpty = " + allEmpty);


        boolean nullOrEmpty = Arrays.isNullOrEmpty(new String[]{"a", "b", "c"});
        System.out.println("nullOrEmpty = " + nullOrEmpty);


        // 定义一些要检查的对象
        String obj1 = "Hello";
        String obj2 = null;
        String obj3 = "World";

        // 将对象放入数组中
        Object[] objects = {obj1, obj2, obj3};

        // 检查是否存在任何对象为 null
        boolean anyNull = Arrays.asList(objects).stream().anyMatch(Objects::isNull);

        ///
        boolean isNullOrEmpty = Arrays.isNullOrEmpty(new String[]{"1", "2", null});
        System.out.println("isNullOrEmpty = " + isNullOrEmpty);

//        StringUtils.isEmpty(null) = true
//        StringUtils.isEmpty("") = true
//        StringUtils.isEmpty(" ") = false
//        StringUtils.isEmpty("bob") = false


//        StringUtils.isNotEmpty(null) = false
//        StringUtils.isNotEmpty("") = false
//        StringUtils.isNotEmpty(" ") = true
//        StringUtils.isNotEmpty("bob") = true

//
//        StringUtils.isAnyEmpty(null) = true
//        StringUtils.isAnyEmpty(null, "foo") = true
//        StringUtils.isAnyEmpty("", "bar") = true
//        StringUtils.isAnyEmpty("bob", "") = true
//        StringUtils.isAnyEmpty("  bob  ", null) = true
//        StringUtils.isAnyEmpty(" ", "bar") = false
//        StringUtils.isAnyEmpty("foo", "bar") = false

    }


}
