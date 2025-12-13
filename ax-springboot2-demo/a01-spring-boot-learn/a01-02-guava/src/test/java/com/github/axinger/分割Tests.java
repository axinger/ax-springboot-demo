package com.github.axinger;

import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName SplitterTest.java
 * @description TODO
 * @createTime 2022年03月22日 17:22:00
 */

public class 分割Tests {

    @Test
    void test1() {
        String str = "1,2,3,4,5";
        List<String> list = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(str);
        System.out.println("list = " + list);

    }
}
