package com.github.axinger;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

public class RandomUtilTests {

    @Test
    void test() {
        /// 随机数
        System.out.println(RandomUtil.randomInt(1, 10));
    }
}
