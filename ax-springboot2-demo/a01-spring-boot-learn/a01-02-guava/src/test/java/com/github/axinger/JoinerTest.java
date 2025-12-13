package com.github.axinger;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName GuavaStringTest.java
 * @description TODO
 * @createTime 2022年03月21日 13:20:00
 */

public class JoinerTest {

    @Test
    void join() {
        List<String> list = Lists.newArrayList("d");
        list.add("a");
        list.add(null);
        list.add("b");
        list.add("c");

        // 有null 报错
//        String result = Joiner.on("-").join(list);
//        System.out.println("Joiner = "+result);

        // 跳过 null 值
        System.out.println("Joiner = " + Joiner.on("-").skipNulls().join(list));


        System.out.println("Joiner = " + Joiner.on("-").useForNull("空值").join(list));


        Map<String, String> testMap = Maps.newLinkedHashMap();
        testMap.put("name", "jim");
        testMap.put("age", "23");
        testMap.put("high", "3");
        String mapString = Joiner.on("-").withKeyValueSeparator(":").join(testMap);

        System.out.println("拼接Map: = " + mapString);


    }

    @Test
    void join2() {
        List<String> list = Lists.newArrayList("d");
        list.add("a");
        list.add("b");
        list.add("c");

        String result = Joiner.on("-").join(list);
        System.out.println("Joiner = " + result);


    }

    @Test
    void test_Splitter() {
        String str = "1-2-3--4-5- 6";
        List<String> list = Splitter.on("-").splitToList(str);
        System.out.println("分割 = " + list);


        List<String> list3 = Splitter.on("-").omitEmptyStrings().splitToList(str);
        // [1, 2, 3, 4, 5,  6]
        System.out.println("分割 去除没有的,会有空格 = " + list3);

        List<String> list2 = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(str);
        // [1, 2, 3, 4, 5, 6]
        System.out.println("分割 去除空格 trimResults = " + list2);


        String str2 = "key1=54&key2=28";
        Map<String, String> map = Splitter.on("&").withKeyValueSeparator("=").split(str2);
        System.out.println("解析参数格式=" + map);

    }

}
