package com.github.axinger;

import lombok.NonNull;
import org.junit.Test;

public class NonNullTest {

    // Lombok 代码
    public void action(@NonNull String name, int age) {
        System.out.println("name = " + name + ", age = " + age);
    }

    @Test
    public void test1() {
        action("jim", 12);
    }

    @Test
    public void test2() {
        action(null, 12);
    }

}
