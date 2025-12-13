package com.github.axinger;

import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

public class VersionTests {

    /**
     * 返回值 < 0：表示调用对象小于参数对象
     * 返回值 = 0：表示调用对象等于参数对象
     * 返回值 > 0：表示调用对象大于参数对象
     */
    @Test
    void test1() {

        // -1
        int compare = StrUtil.compareVersion("1.12.1", "1.12.1c");
        System.out.println("1.12.1 >= 1.12.1c = " + (compare >= 0));

        int compare1 = StrUtil.compareVersion("1.0.2", "1.0.10");
        System.out.println("1.0.2 >= 1.0.10 = " + (compare1 >= 0));

        // 1<2
        int compare2 = StrUtil.compareVersion("1", "2");
        System.out.println("1 >= 2 = " + (compare2 >= 0));

        int compare3 = StrUtil.compareVersion("1.1.0", "1.11.0");
        System.out.println("1.1.0 >= 1.11.0 = " + (compare3 >= 0));

        // 1=1 => 0
        int compare4 = StrUtil.compareVersion("1.0", "1");
        System.out.println("1.0 > 1 = " + (compare4 >= 0));

        // 2>1  => 1
        int compare5 = StrUtil.compareVersion("2", "1");
        System.out.println("2 >1 = " + (compare5 >= 0));

    }
}
