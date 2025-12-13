package com.github.axinger;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RetryableTests {

    @SneakyThrows
    @Test
    void test1() {

        String s = test2();
        System.out.println("s = " + s);


        TimeUnit.SECONDS.sleep(20);
    }


    @Retryable(value = {RuntimeException.class}, maxAttempts = 2, backoff = @Backoff(delay = 2000L, multiplier = 1))
    public String test2() {

        System.out.println("test2===");

        if (true) {
            throw new RuntimeException("test2");
        }

        return "test2";
    }


    @Recover
    @Retryable(value = {IOException.class}, maxAttempts = 2, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public String test3(RuntimeException e) throws IOException {
        System.out.println("test3===");

        if (true) {
            throw new IOException("test3");
        }
        return "test3";
    }

    @Recover
    public String test4(IOException e) {
        System.out.println("test4===");
        return "test4";
    }
}
