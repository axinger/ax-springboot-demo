package com.github.axinger.config;

import com.github.axinger.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
//@Order(2)
@Configuration
@EnableBatchProcessing
public class BatchJobConfig3 {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private DataSource businessDataSource;

    @Bean
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(@Nonnull StepExecution stepExecution) {
                // 获取作业参数
                String selectDate = stepExecution.getJobExecution().getJobParameters().getString("selectDate");
                log.info("监听步骤获取参数: {}", selectDate);

                // 将参数存储到 ExecutionContext 中，供后续步骤使用
//                stepExecution.getExecutionContext().putLong("time", timeParam);
            }

            @Override
            public ExitStatus afterStep(@Nonnull StepExecution stepExecution) {
                return null; // 可根据需求返回 exitStatus
            }
        };
    }


    @Bean
    @StepScope
    public JdbcCursorItemReader<User> itemReader(@Value("#{jobParameters['selectDate']}") String selectDate,
                                                 @Value("#{stepExecutionContext['limit']}") Integer limit,
                                                 @Value("#{stepExecutionContext['offset']}") Integer offset,
                                                 @Value("#{stepExecutionContext['time']}") Long time) {
        String sql = "SELECT * FROM user LIMIT ? OFFSET ?";
        log.info("读数据启动参数={},分页参数={}，{}", selectDate, limit, offset);
        return new JdbcCursorItemReaderBuilder<User>()
                .name("personItemReader")
                .dataSource(businessDataSource)
                .sql(sql)
                .queryArguments(limit, offset)
                .beanRowMapper(User.class)
                .build();
    }


    // 写
    @Bean
    @StepScope
    public ItemWriter<User> itemWriter(@Value("#{jobParameters['selectDate']}") String selectDate) {
        return items -> {
            log.info("读数据获取启动参数={}", selectDate);
            for (User item : items) {
                String sql = "INSERT INTO `user_2` (`id`, `name`, `email`) VALUES (?, ?, ?);";
                jdbcTemplate.update(sql, preparedStatement -> {
                    preparedStatement.setInt(1, item.getId());
                    preparedStatement.setString(2, item.getName());
                    preparedStatement.setString(3, item.getEmail());
                });
            }
        };
    }


    // 分区
    @Bean
    @StepScope
    public Partitioner partitioner(@Value("#{jobParameters['selectDate']}") String selectDate) {
        return gridSize -> {
            log.info("分区中获取启动参数={}", selectDate);
            Map<String, ExecutionContext> partitionMap = new HashMap<>();
            Integer totalRecords = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user", Integer.class);
            if (Optional.ofNullable(totalRecords).isEmpty()) {
                return partitionMap;
            }
            int rangeSize = (int) Math.ceil(totalRecords * 1.0 / gridSize);
            for (int i = 0; i < gridSize; i++) {
                ExecutionContext context = new ExecutionContext();
                int startIndex = i * rangeSize;
                int endIndex = (i + 1) * rangeSize - 1;
                if (i == gridSize - 1) {
                    endIndex = totalRecords - 1;  // Ensure the last partition handles any remaining records
                }
                int limit = endIndex - startIndex + 1;
                context.putInt("limit", limit);
                context.putInt("offset", startIndex);
                partitionMap.put("partition_" + i, context);
            }
            return partitionMap;
        };
    }

    // 子步骤
    @Bean
    public Step userSubStep() {
        return stepBuilderFactory.get("subStep")
                .<User, User>chunk(100)
                .reader(itemReader(null, null, null, null))
                .writer(itemWriter(null))
                .listener(stepExecutionListener())
                .build();
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setGridSize(3);
        handler.setTaskExecutor(new SimpleAsyncTaskExecutor("多线程-"));
        handler.setStep(userSubStep());
        try {
            handler.afterPropertiesSet();
        } catch (Exception e) {
            log.error("分区错误={}", e.getMessage());
            throw new RuntimeException(e);
        }
        return handler;
    }

    // 主步骤
    @Bean
    public Step userMasterStep() {
        return stepBuilderFactory.get("masterStep")
                .partitioner("masterP", partitioner(null))
                .partitionHandler(partitionHandler())
                .build();
    }


    @Bean("userJob")
    public Job userJob() {
        return jobBuilderFactory.get("addUserJob2")
                .start(userMasterStep())
                .build();
    }
}
