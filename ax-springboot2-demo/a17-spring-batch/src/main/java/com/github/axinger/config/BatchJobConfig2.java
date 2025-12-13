//package com.github.axinger.config;
//
//import com.github.axinger.domain.User;
//import com.github.axinger.item.UserProcessor;
//import com.github.axinger.item.UserWriter;
//import com.github.axinger.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.batch.MyBatisBatchItemWriter;
//import org.mybatis.spring.batch.MyBatisCursorItemReader;
//import org.mybatis.spring.batch.MyBatisPagingItemReader;
//import org.springframework.batch.core.*;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.partition.PartitionHandler;
//import org.springframework.batch.core.partition.support.Partitioner;
//import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.scope.context.StepSynchronizationManager;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Order(2)
//@Configuration
//@EnableBatchProcessing
//public class BatchJobConfig2 {
//
//    @Resource
//    private JobBuilderFactory jobBuilderFactory;
//
//    @Resource
//    private StepBuilderFactory stepBuilderFactory;
/// /
/// /    @Bean
/// /    @StepScope
/// /    public MyBatisPagingItemReader<User> itemReader(
/// /            @Value("#{stepExecutionContext['lastId']}") Long lastId,
/// /            @Value("#{stepExecutionContext['pageSize']}") Integer pageSize) {
/// /
/// /        MyBatisPagingItemReader<User> itemReader = new MyBatisPagingItemReader<>();
/// /        itemReader.setSqlSessionFactory(sqlSessionFactory);
/// /        itemReader.setQueryId("com.github.axinger.mapper.UserMapper.selectPageList");
/// /
/// /        Map<String, Object> params = new HashMap<>();
/// /        params.put("lastId", lastId); // 上次分页的最后一条 ID
/// /        params.put("pageSize", pageSize);
/// /        itemReader.setParameterValues(params);
/// /
/// /        return itemReader;
/// /    }
//
/// /    public MyBatisCursorItemReader<User> cursorItemReader(){
/// /
/// /         new MyBatisCursorItemReader<>().var;
/// /    }
//
//
//    @Autowired
//    private DataSource dataSource;
//
//@Autowired
//    private JobRepository jobRepository;
//    @Autowired
//    private PlatformTransactionManager platformTransactionManager;
//    @Bean
//    @StepScope
//    public JdbcCursorItemReader<User> itemReader() {
//        return new JdbcCursorItemReaderBuilder<User>()
//                .name("personItemReader")
//                .dataSource(dataSource)
//                .sql("select * from user")
//                .beanRowMapper(User.class)
//                .build();
//    }
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @Bean
//    public ItemWriter<User> itemWriter() {
//        return items -> {
//            for (User item : items) {
/// /                System.out.println("item = " + item);
//
//                String sql = "INSERT INTO `user_2` (`id`, `name`, `email`) VALUES (?, ?, ?);";
//                jdbcTemplate.update(sql, preparedStatement -> {
//                    preparedStatement.setInt(1, item.getId());
//                    preparedStatement.setString(2, item.getName());
//                    preparedStatement.setString(3, item.getEmail());
//                });
//
//            }
//
//        };
//    }
//
//    @Autowired
//    SqlSessionFactory sqlSessionFactory;
//
/// /    @Bean
/// /    public MyBatisBatchItemWriter<User> itemWriter() {
/// /        MyBatisBatchItemWriter<User> itemWriter = new MyBatisBatchItemWriter<>();
/// /        itemWriter.setSqlSessionFactory(sqlSessionFactory);
/// /        itemWriter.setStatementId("com.github.axinger.mapper.UserMapper.saveOne");
/// /        return itemWriter;
/// /    }
//
//
//    @Bean
//    @StepScope
//    public Step subStep() {
//        return stepBuilderFactory.get("subStep")
//                .<User, User>chunk(50)
/// /                .reader(new UserReader(userService))
/// /                .listener(readListener())
/// /                .reader(itemReader(null, null))
//                .listener(readListener())
//                .reader(itemReader())
/// /                .processor(userProcessor)
/// /                .writer(userWriter)
//                .writer(itemWriter())
/// /                .listener(itemWriteListener()) // 注册监听器
/// /                .transactionManager(transactionManager())  // 事务管理器
/// /                .transactionManager(platformTransactionManager)
//                .build();
//    }
//
//
//    @Bean
//    public Partitioner partitioner() {
//        return new Par2();
//    }
//
//    @Bean
//    public PartitionHandler partitionHandler() {
//        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
//        handler.setGridSize(3);
/// /        handler.setTaskExecutor(new SimpleAsyncTaskExecutor("多线程-"));
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
//                .partitioner("masterP", partitioner())
/// /                .gridSize(4) // 设置并发分区数量
/// /                .taskExecutor(new SimpleAsyncTaskExecutor())
//                .partitionHandler(partitionHandler())
//                .build();
//    }
//
//
//    @Bean("userJob")
//    public Job job1() {
//        return jobBuilderFactory.get("addUserJob2")
//                .start(masterStep())
/// /                .incrementer(new RunIdIncrementer())
//                .build();
//    }
//
//    @Bean
//    public ItemReadListener<User> readListener() {
//
//        return new ItemReadListener<>() {
//
//            @Override
//            public void beforeRead() {
//
//            }
//
//            @Override
//            public void afterRead(User item) {
//                if (item.getId()==1) {
//
//                log.info("读取数据={}", item);
//                }
//
//            }
//
//            @Override
//            public void onReadError(Exception ex) {
//
//            }
//        };
//    }
//
/// /    @Bean
/// /    public ItemWriteListener<User> itemWriteListener() {
/// /        return new ItemWriteListener<User>() {
/// /            @Override
/// /            public void beforeWrite(List<? extends User> items) {
/// /                // 可选：在写入前执行某些逻辑
/// /                if (!items.isEmpty()) {
/// /                    // 获取最后一个写入记录的主键 ID
/// /                    Integer lastProcessedId = items.get(items.size() - 1).getId();
/// /
/// /                    // 将最后处理的 ID 写入 StepExecutionContext
/// /                    StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
/// /                    stepExecution.getExecutionContext().putInt("lastId", lastProcessedId);
/// /
/// /                    log.info("Updated lastId in StepExecutionContext: {}", lastProcessedId);
/// /                }
/// /            }
/// /
/// /            @Override
/// /            public void afterWrite(List<? extends User> items) {
/// /
/// /            }
/// /
/// /            @Override
/// /            public void onWriteError(Exception exception, List<? extends User> items) {
/// /                log.error("Error occurred while writing items: {}", exception.getMessage(), exception);
/// /            }
/// /        };
/// /    }
//
//}
