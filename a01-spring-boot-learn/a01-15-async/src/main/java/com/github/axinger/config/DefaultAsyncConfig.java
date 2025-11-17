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
//@EnableAspectJAutoProxy(exposeProxy = true)
@Slf4j
public class DefaultAsyncConfig implements AsyncConfigurer {

    @Override
    @Primary
    @Bean
    public Executor getAsyncExecutor() {


//        ThreadPoolExecutor(); 这个无法定义名称

        int cpuNum = 4;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程池数量
        executor.setCorePoolSize(cpuNum);
        // 配置最大线程池数量
        executor.setMaxPoolSize(cpuNum * 2);
        /// 线程池所使用的缓冲队列
        executor.setQueueCapacity(2);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        executor.setAwaitTerminationSeconds(60);
        // 空闲线程存活时间
        executor.setKeepAliveSeconds(60);
        // 等待任务在关机时完成--表明等待所有线程执行完
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池名称前缀
        executor.setThreadNamePrefix("️️👉️默认线程池-👈");
        // 线程池拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 线程池初始化
        executor.initialize();
        return executor;

    }

    @Override
    /**
     * 自定义异常处理类
     * @author hry
     *
     */
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

