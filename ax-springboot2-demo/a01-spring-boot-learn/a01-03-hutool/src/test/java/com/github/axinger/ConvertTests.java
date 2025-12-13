package com.github.axinger;

import cn.hutool.core.convert.Convert;
import org.junit.jupiter.api.Test;

public class ConvertTests {

    @Test
    void test1() {
        int a = 1;
//aStr为"1"
        String aStr = Convert.toStr(a);

        long[] b = {1, 2, 3, 4, 5};
//bStr为："[1, 2, 3, 4, 5]"
        String bStr = Convert.toStr(b);

    }

    @Test
    void test2() {
        // ONE HUNDRED AND CENTS TWENTY THREE ONLY
        String format = Convert.numberToWord(100.23);
        System.out.println("format = " + format);
    }

}
