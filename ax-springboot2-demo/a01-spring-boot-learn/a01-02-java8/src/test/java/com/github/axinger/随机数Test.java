package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("all")
public class 随机数Test {

    @Test
    public void test1() {

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int j = random.nextInt(10);
            System.out.println(j);
        }
    }

    @Test
    public void test2() {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            int j = random.nextInt(10);
            System.out.println(j);
        }
    }

    @Test
    public void test3() {
        //内部调用Random
        for (int i = 0; i < 10; i++) {
            double random = Math.random();
            System.out.println(random);
        }
    }

    @Test
    public void test4() {
        ThreadLocalRandom current = ThreadLocalRandom.current();

        for (int i = 0; i < 10; i++) {
            int j = current.nextInt(10);
            System.out.println(j);
        }

    }
}
