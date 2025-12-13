package com.github.axinger._07fork_join;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

class MyTask extends RecursiveTask<Integer> {

    // 拆分,差值不超过10
    private static final Integer VALUE = 10;
    private final Integer begin;
    private final Integer end;
    private Integer result = 0;

    public MyTask(Integer begin, Integer end) {
        this.begin = begin;
        this.end = end;
    }

    // 拆分合并
    @Override
    protected Integer compute() {
        // 判断
        if (end - begin <= VALUE) {
            ///相加
            for (int i = begin; i <= end; i++) {
                result = result + i;
            }
        } else {
            // 继续拆分
            // 获取中间值
            Integer middle = (begin + end) / 2;
            // 拆分左边
            MyTask task1 = new MyTask(begin, middle);

            // 拆分右边
            MyTask task2 = new MyTask(middle + 1, end);

            task1.fork();
            task2.fork();

            result = task1.join() + task2.join();
        }


        return result;
    }
}


/**
 * 复杂任务分割多个小任务
 */
public class ForkJoinDemo {

    // 1+2+....+100,相加两个数,差值超过10
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建对象

        MyTask myTask = new MyTask(0, 100);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        /// 获得最终合并之后的结果

        Integer val = forkJoinTask.get();

        System.out.println("forkJoinTask.get() = " + val);

        forkJoinPool.shutdown();

    }
}
