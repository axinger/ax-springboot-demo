package com.github.axinger.cllback;


import org.junit.jupiter.api.Test;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName Test.java
 * @description TODO
 * @createTime 2022年03月27日 00:07:00
 */

public class CallBackTest {

    @Test
    void test() {

        Caller call = new Caller();
        call.setCallfuc(new B());
        call.call();
    }
}
