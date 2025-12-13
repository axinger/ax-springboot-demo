package com.github.axinger;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

public class AssertTests {

    @Test
    void test() {

        String a = null;
        // String a = "1";
        // 表达式如果是true程序继续执行，如果是false就会报错告诉你这里错了
        Assert.notBlank(a, "a不能为空{}{}", 1, 2);
        System.out.println("222222222222222");
    }
}
