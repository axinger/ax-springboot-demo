package com.github.axinger;

import com.github.axinger.model.DelegateExample;
import org.junit.Test;

public class DelegateTests {


    @Test
    public void test1() {
        DelegateExample example = new DelegateExample();
        // 调用其他内方法,不用继承
        example.test();
    }
}
