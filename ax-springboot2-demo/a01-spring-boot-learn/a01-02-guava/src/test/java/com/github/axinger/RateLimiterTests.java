package com.github.axinger;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class RateLimiterTests {

    @Test
    void test1() {

//        RateLimiter limiter = RateLimiter.create(1,1, TimeUnit.SECONDS);
        // 首先创建一个限流器，参数代表每秒生成的令牌数
        RateLimiter limiter = RateLimiter.create(1);

        for (int i = 0; i < 10; i++) {
            // limiter.acquire以阻塞的方式获取令牌。
            // 当然也可以通过tryAcquire(int permits, long timeout, TimeUnit unit)来设置等待超时时间的方式获取令牌，
            // 如果超timeout为0，则代表非阻塞，获取不到立即返回
//            double waitTime = limiter.acquire(2);
//            System.out.println("cutTime=" + LocalDateTime.now() + " acq:" + i + " waitTime:" + waitTime);

            //不是阻塞
            if (limiter.tryAcquire(4, TimeUnit.SECONDS)) {
                System.out.println(LocalDateTime.now() + "获取到令牌" + i + "速率" + limiter.getRate());
            } else {
                System.out.println("没有获取到令牌" + i);
            }
        }


    }
}
