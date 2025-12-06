package com.github.axinger.config;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

// 测试的时候请打开这个 `@Configuration`，并关闭其他 Mode 的配置
@Configuration
public class MyQuartzAutoConfigurationMode2 {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private DynamicDataSourceProperties properties;

    //3.4.0版本及以上使用以下方式注入,老版本请阅读文档  进阶-手动注入多数据源
    @Primary
    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
        dataSource.setPrimary(properties.getPrimary());
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }

//    @QuartzDataSource
//    @Bean
//    public DataSource quartzDataSource() {
//        return dataSourceProperties.initializeDataSourceBuilder().build();
//    }

    //如果需要使用动态数据源里的某个数据源则打开以下配置，关闭上面配置。
    @Bean
    @QuartzDataSource
    public DataSource quartzDataSource(DataSource dataSource) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getDataSource("slave_quartz");
    }
}
