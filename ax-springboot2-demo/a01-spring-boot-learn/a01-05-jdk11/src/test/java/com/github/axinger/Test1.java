package com.github.axinger;

public interface Test1 {

    // 接口 可以有私有方法,不用 default 修饰
    private void test1() {

    }

    default void test2() {

    }

    void test3();

}
