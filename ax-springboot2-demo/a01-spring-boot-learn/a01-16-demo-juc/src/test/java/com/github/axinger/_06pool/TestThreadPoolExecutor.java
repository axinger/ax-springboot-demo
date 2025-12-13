package com.github.axinger._06pool;

import java.util.concurrent.*;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestThreadPoolExecutor.java
 * @Description TODO
 * @createTime 2022年02月12日 21:35:00
 */

public class TestThreadPoolExecutor {
    /*
    corePoolSize:常驻线程数量
    maximumPoolSize: 最大
    keepAliveTime: 是否存活
    unit:保存存活时间
    workQueue: 阻塞队列
    threadFactory: 线程工程
    handler: 拒绝策略

        public ThreadPoolExecutor(int corePoolSize,
                               int maximumPoolSize,
                               long keepAliveTime,
                               TimeUnit unit,
                               BlockingQueue<Runnable> workQueue,
                               ThreadFactory threadFactory,
                               RejectedExecutionHandler handler)

maximumPoolSize在cpu密级型项目中请配置成 cup核心数+1==>Runtime.getRuntime().availableProcessors()+1
maximumPoolSize在io密级型项目中请配置成 cpu核心数/阻塞系数


     * */

    public static void main(String[] args) {

        test_ThreadPoolExecutor();
    }

    static void test_ThreadPoolExecutor() {
        ExecutorService executorService = new ThreadPoolExecutor(2, 2,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());// 指定拒绝策略

        // submit有返回值，而execute没有
        final Future<?> future = executorService.submit(() -> {

            TimeUnit.SECONDS.sleep(2);
            return "jim";
        });
//        executorService.execute(()->{
//
//        });


        System.out.println("异步过程=============");
        try {
            System.out.println("future.get() = " + future.get());


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}
