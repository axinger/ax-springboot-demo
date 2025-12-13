package com.github.axinger;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TotalTimeTests.java
 * @Description TODO
 * @createTime 2022年02月20日 20:37:00
 */

@SpringBootTest
public class TotalTimeTests {

    @Test
    public void timeLong() throws InterruptedException {
        // StopWatch 用来计时的
        StopWatch watch = new StopWatch();
        watch.start();
        TimeUnit.SECONDS.sleep(3);
        watch.stop();
        System.out.println("计算时长 = " + watch.getTotalTimeSeconds());
    }
}
