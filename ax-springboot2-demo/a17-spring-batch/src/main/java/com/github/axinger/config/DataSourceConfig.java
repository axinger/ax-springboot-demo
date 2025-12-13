package com.github.axinger.config;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;


    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Bean
    @Primary
    @Order(1)
    public DataSource businessDataSource() {

//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://hadoop102:3306/ax_test3?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&&zeroDateTimeBehavior=CONVERT_TO_NULL&&serverTimezone=GMT%2B8&&nullCatalogMeansCurrent=true");
//        dataSource.setUsername("root");
//        dataSource.setPassword("123456");
        DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource().get("db_ax_sub");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());

        return dataSource;
    }

    @Bean
    @BatchDataSource
    @Order(1)
    public DataSource batchDataSource() {

        DataSourceProperty dataSourceProperty = dynamicDataSourceProperties.getDatasource().get("spring_batch");

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceProperty.getDriverClassName());
        dataSource.setUrl(dataSourceProperty.getUrl());
        dataSource.setUsername(dataSourceProperty.getUsername());
        dataSource.setPassword(dataSourceProperty.getPassword());
        return dataSource;


//        HikariDataSourceCreator dataSourceCreator = new HikariDataSourceCreator();
//        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
//
//
//        return dataSource;


//        return dataSourceProperties.initializeDataSourceBuilder().build();
    }


}
