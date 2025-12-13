package com.github.axinger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通过实现AsyncConfigurer自定义异常线程池，包含异常处理
 *
 * @author hry
 */
@Configuration
@EnableAsync
@Slf4j
public class MyDefaultExecutor implements AsyncConfigurer {
    // https://blog.csdn.net/qq_40428665/article/details/121680026
    @Primary
    @Bean
    @Override
    public Executor getAsyncExecutor() {

//        ThreadPoolExecutor(); 这个无法定义名称
/**
 * 1、corePoolSize: 核心线程数
 *
 * 这个应该是最重要的参数了，所以如何合理的设置它十分重要。
 *
 *     核心线程会一直存活，及时没有任务需要执行。
 *     当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理。
 *     设置allowCoreThreadTimeout=true（默认false）时，核心线程会超时关闭。
 *
 *     CPU密集型：corePoolSize = CPU核数 + 1
 *
 *      IO密集型：corePoolSize = CPU核数 * 2
 */

/**
 * 计算密集型
 *     计算密集型，顾名思义就是应用需要非常多的CPU计算资源，在多核CPU时代，我们要让每一个CPU核心都参与计算，将CPU的性能充分利用起来，这样才算是没有浪费服务器配置，如果在非常好的服务器配置上还运行着单线程程序那将是多么重大的浪费。对于计算密集型的应用，完全是靠CPU的核数来工作，所以为了让它的优势完全发挥出来，避免过多的线程上下文切换，比较理想方案是：
 *     线程数 = CPU核数+1
 *     也可以设置成CPU核数*2，这还是要看JDK的使用版本，以及CPU配置(服务器的CPU有超线程)。对于JDK1.8来说，里面增加了一个并行计算，计算密集型的较理想线程数 = CPU内核线程数*2
 *
 * IO密集型 - web开发
 *     对于IO密集型的应用，就很好理解了，我们现在做的开发大部分都是WEB应用，涉及到大量的网络传输，不仅如此，与数据库，与缓存间的交互也涉及到IO，一旦发生IO，线程就会处于等待状态，当IO结束，数据准备好后，线程才会继续执行。因此从这里可以发现，对于IO密集型的应用，我们可以多设置一些线程池中线程的数量，这样就能让在等待IO的这段时间内，线程可以去做其它事，提高并发处理效率。
 *     那么这个线程池的数据量是不是可以随便设置呢？当然不是的，请一定要记得，线程上下文切换是有代价的。目前总结了一套公式，对于IO密集型应用：
 *     线程数 = CPU核心数/(1-阻塞系数)
 *     这个阻塞系数一般为0.8~0.9之间，也可以取0.8或者0.9。套用公式，对于双核CPU来说，它比较理想的线程数就是20，当然这都不是绝对的，需要根据实际情况以及实际业务来调整。
 *     final int poolSize = (int)(cpuCore/(1-0.9))
 */

        int cpuCore = Runtime.getRuntime().availableProcessors();
        final int poolSize = (int) (cpuCore / (1 - 0.9));

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数目
        executor.setCorePoolSize(65);
        // 指定最大线程数
        executor.setMaxPoolSize(65);
        // 队列中最大的数目
        executor.setQueueCapacity(650);
        // 线程名称前缀
        executor.setThreadNamePrefix("🐛🐛🐛自定义线程池-");
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        // 对拒绝task的处理策略
        // DiscardOldestPolicy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程空闲后的最大存活时间
        executor.setKeepAliveSeconds(60);
        // 加载
        executor.initialize();
        return executor;

    }


    /**
     * 自定义异常处理类
     *
     * @author hry
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (Throwable throwable, Method method, Object... obj) -> {
            log.info("异常处理类 - " + throwable.getMessage());
            log.info("异常处理类 - " + method.getName());
            for (Object param : obj) {
                log.info("异常处理类 Parameter value - " + param);
            }
        };

    }


}

