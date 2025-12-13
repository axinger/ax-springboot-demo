package com.github.axinger._10CompletableFuture;

import java.util.concurrent.*;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName CompletableFutureTests.java
 * @Description TODO
 * @createTime 2022年02月12日 21:42:00
 * https://www.jianshu.com/p/dff9063e1ab6
 */

class TestCompletableFuture {
    public static void main(String[] args) {
        // runAsync 和 supplyAsync 方法的区别是runAsync返回的CompletableFuture是没有返回值的。
        test_supplyAsync();

    }

    static void test_runAsync() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Hello");
        });

        try {
            System.out.println("future.get() = " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("CompletableFuture");
    }

    static void test_supplyAsync() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("进入异步===============");
            System.out.println("当前线程：" + Thread.currentThread().getName());
            System.out.println("executorService 是否为守护线程 :" + Thread.currentThread().isDaemon());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Hello");
            return "Hello";
        });

        // 如果执行成功:
        future.thenAccept((result) -> {
            System.out.println("执行成功 = " + result);
            // 同步,无返回值
        });

        future.thenAcceptAsync(e -> {
            // 异步,无返回值
        });

        future.thenApplyAsync(e -> {

            // 异步,需要返回值
            return "";
        });
        future.thenApply(e -> {

            // 同步,需要返回值
            return "";
        });

        future.whenComplete((k, v) -> {
            // 结果
            System.out.println("whenComplete k = " + k + ", v = " + v);
        });


        // future.get()在等待执行结果时，程序会一直block，如果此时调用complete(T t)会立即执行。
//        future.complete("World");

        // future.get() 是阻塞式
//        try {
//            System.out.println("future.get() = " + future.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        System.out.println("CompletableFuture");

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void test_ExecutorCompletionService() {

        ExecutorService executorService = new ThreadPoolExecutor(2, 2,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());

        final ExecutorCompletionService service1 = new ExecutorCompletionService(executorService);
        try {
            final Object o = service1.take().get();
            System.out.println("service1 = " + o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        {

            final ExecutorCompletionService service2 = new ExecutorCompletionService(executorService);
            try {
                final Object o = service2.take().get();
                System.out.println("service2 = " + o);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }
}


class Both {

    private static final ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行任务 1");

            return "1";
        }, service);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行任务 2");

            return "2";
        }, service);

        future1.runAfterBothAsync(future2, () -> {

            System.out.println("runAfterBothAsync");

        });

        future1.thenAcceptBothAsync(future2, (res1, res2) -> {

            System.out.println("thenAcceptBothAsync");

        });

        future1.thenCombineAsync(future2, (res1, res2) -> {

            System.out.println("thenCombineAsync");

            return "合并" + res1 + "-" + res2;

        }).whenComplete((res, e) -> {
            System.out.println("thenCombineAsync = " + res);
        });


    }

}


class Either {

    private static final ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行任务 1");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "1";
        }, service);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行任务 2");

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "2";
        }, service);

        future1.runAfterEitherAsync(future2, () -> {
            System.out.println("runAfterEitherAsync");
        });

        future1.acceptEitherAsync(future2, (res1) -> {
            System.out.println("acceptEitherAsync = " + res1);
        });

        future1.applyToEitherAsync(future1, res1 -> {

            return "任何一个合并 " + res1;
        }).whenComplete((s, throwable) -> {

            System.out.println("applyToEitherAsync = " + s);
        });
    }

}


class All {

    private static final ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("执行任务 1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "All1";
        }, service);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("执行任务 2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "All2";
        }, service);


        CompletableFuture.allOf(future1, future2).whenComplete((unused, throwable) -> {

            System.out.println("unused = " + unused);
        }).join();

        System.out.println("allOf future1 = " + future1.get() + ", future2 = " + future2.get());
    }

}


class Any {

    private static final ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("执行任务 1");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Any 1";
        }, service);

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("执行任务 2");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Any 2";
        }, service);


        final Object join = CompletableFuture.anyOf(future1, future2).whenComplete((unused, throwable) -> {

            System.out.println("unused = " + unused);
        }).join();

        System.out.println("anyOf = " + join);
    }

}

