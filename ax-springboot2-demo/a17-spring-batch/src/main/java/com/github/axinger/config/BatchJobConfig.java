//package com.github.axinger.config;
//
//import com.baomidou.mybatisplus.core.batch.MybatisBatch;
//import com.github.axinger.domain.User;
//import com.github.axinger.item.UserProcessor;
//import com.github.axinger.item.UserReader;
//import com.github.axinger.item.UserWriter;
//import com.github.axinger.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.batch.MyBatisBatchItemWriter;
//import org.mybatis.spring.batch.MyBatisPagingItemReader;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.partition.PartitionHandler;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Order(2)
//@Configuration
//@EnableBatchProcessing
//public class BatchJobConfig {
//
//    @Resource
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Resource
//    private StepBuilderFactory stepBuilderFactory;
//
/// /    @Autowired
/// /    private UserReader userReader;
//
//    @Autowired
//    private UserProcessor userProcessor;
//
//    @Autowired
//    private UserWriter userWriter;
//
/// /    @Bean("userJob")
/// /    public Job job1() {
/// /        return jobBuilderFactory.get("addUserJob")
/// /                .start(step1())
/// /                .incrementer(new RunIdIncrementer())
/// /
/// /                .build();
/// /    }
//
//    @Autowired
//    private UserService userService;
//
//
//    @Bean
//    @StepScope
//    public MyBatisPagingItemReader<User> itemReader(
//            @Value("#{stepExecutionContext[pageNumber]}") Integer pageNumber,
//            @Value("#{stepExecutionContext[pageSize]}") Integer pageSize ) {
//
//        MyBatisPagingItemReader<User> itemReader = new MyBatisPagingItemReader<>();
//        itemReader.setSqlSessionFactory(sqlSessionFactory);
/// /        itemReader.setPageSize(pageSize); //不要，会循环
//        Map<String, Object> map = new HashMap<>();
/// /        map.put("pageNumber", pageNumber);
//        map.put("pageSize", pageSize);
//        map.put("offset", pageSize*pageNumber);
//        itemReader.setParameterValues(map);
//        itemReader.setQueryId("com.github.axinger.mapper.UserMapper.selectPageList");
//        return itemReader;
//    }
//
//
//    @Autowired
//    SqlSessionFactory sqlSessionFactory;
//
//    @Bean
//    public MyBatisBatchItemWriter<User> itemWriter() {
//        MyBatisBatchItemWriter<User> itemWriter = new MyBatisBatchItemWriter<>();
/// /        itemWriter.setSqlSessionFactory();
//        itemWriter.setSqlSessionFactory(sqlSessionFactory);
//        itemWriter.setStatementId("com.github.axinger.mapper.UserMapper.saveOne");
//
//        return itemWriter;
//    }
//
//
//    @Bean
//    public Step subStep() {
//        return stepBuilderFactory.get("subStep")
//                .<User, User>chunk(2)
/// /                .reader(new UserReader(userService))
//                .reader(itemReader(null, null))
/// /                .processor(userProcessor)
/// /                .writer(userWriter)
//                .writer(itemWriter())
//                .build();
//    }
//
//
//    @Bean
//    public Partitioner partitioner() {
//        return new Par2();
//    }
//
/// /    @Bean
/// /    public TaskExecutor taskExecutor() {
/// /        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
/// /        executor.setCorePoolSize(4); // 配置线程池大小
/// /        executor.setMaxPoolSize(4);
/// /        executor.setQueueCapacity(10);
/// /        executor.afterPropertiesSet();
/// /        return executor;
/// /    }
//
//
//    @Bean
//    public PartitionHandler partitionHandler() {
//        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
//        handler.setGridSize(10);
//        handler.setTaskExecutor(new SimpleAsyncTaskExecutor("多线程-"));
/// /        handler.setTaskExecutor(taskExecutor());
//        handler.setStep(subStep());
//        try {
//            handler.afterPropertiesSet();
//        } catch (Exception e) {
//            log.error("分区错误={}", e.getMessage());
//            throw new RuntimeException(e);
//        }
//        return handler;
//    }
//
//    // 主步骤
//    @Bean
//    public Step masterStep() {
//        return stepBuilderFactory.get("masterStep")
//                .partitioner(subStep().getName(), partitioner())
//                .partitionHandler(partitionHandler())
//                .build();
//    }
//
//
//    @Bean("userJob")
//    public Job job1() {
//        return jobBuilderFactory.get("addUserJob2")
//                .start(masterStep())
//                .incrementer(new RunIdIncrementer())
//                .build();
//    }
//}
