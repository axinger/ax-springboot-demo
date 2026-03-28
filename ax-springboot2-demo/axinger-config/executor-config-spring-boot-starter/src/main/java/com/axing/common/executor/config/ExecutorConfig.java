package com.axing.common.executor.config;

import com.axing.common.executor.bea.ExecutorProperties;
import com.axing.common.executor.decorator.MdcTaskDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@AutoConfiguration
@EnableConfigurationProperties(ExecutorProperties.class)
public class ExecutorConfig implements AsyncConfigurer {

    @Autowired
    private ExecutorProperties properties;

    /**
     * log.info("cpu核心线程数={}", availableProcessors);
     * ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
     * //配置核心线程数
     * executor.setCorePoolSize(availableProcessors * 2);
     * //配置最大线程数
     * executor.setMaxPoolSize(2 * 200);
     * //配置队列大小
     * executor.setQueueCapacity(400);
     * //配置线程池中的线程的名称前缀
     * executor.setThreadNamePrefix("自定义默认线程池-");
     * <p>
     * 想要合理配置线程池线程数的大小，需要分析任务的类型，任务类型不同，线程池大小配置也不同。
     * <p>
     * 配置线程池的大小可根据以下几个维度进行分析来配置合理的线程数：
     * <p>
     * 任务性质可分为：CPU密集型任务，IO密集型任务，混合型任务。
     * 任务的执行时长。
     * 任务是否有依赖——依赖其他系统资源，如数据库连接等。
     * CPU密集型任务
     * 尽量使用较小的线程池，一般为CPU核心数+1。
     * 因为CPU密集型任务使得CPU使用率很高，若开过多的线程数，只能增加上下文切换的次数，因此会带来额外的开销。
     * <p>
     * IO密集型任务
     * 可以使用稍大的线程池，一般为2*CPU核心数+1。
     * 因为IO操作不占用CPU，不要让CPU闲下来，应加大线程数量，因此可以让CPU在等待IO的时候去处理别的任务，充分利用CPU时间。
     * <p>
     * 混合型任务
     * 可以将任务分成IO密集型和CPU密集型任务，然后分别用不同的线程池去处理。
     * 只要分完之后两个任务的执行时间相差不大，那么就会比串行执行来的高效。
     * 因为如果划分之后两个任务执行时间相差甚远，那么先执行完的任务就要等后执行完的任务，最终的时间仍然取决于后执行完的任务，而且还要加上任务拆分与合并的开销，得不偿失
     * <p>
     * 依赖其他资源
     * 如某个任务依赖数据库的连接返回的结果，这时候等待的时间越长，则CPU空闲的时间越长，那么线程数量应设置得越大，才能更好的利用CPU。
     *
     * @return
     */
    @Override
    @Bean
    @Primary
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(properties.getCorePoolSize());
        // 配置最大线程数
        executor.setMaxPoolSize(properties.getMaxPoolSize());
        // 配置队列大小
        executor.setQueueCapacity(properties.getQueueCapacity());
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix(properties.getThreadNamePrefix());
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        /// 配置 TaskDecorator 传递 MDC 上下文到子线程
        executor.setTaskDecorator(new MdcTaskDecorator());
        // 执行初始化
        executor.initialize();
        log.info("🎁🎁🎁🎁🎁 cpu核心线程数={}", executor.getCorePoolSize());
        return executor;
    }
}
