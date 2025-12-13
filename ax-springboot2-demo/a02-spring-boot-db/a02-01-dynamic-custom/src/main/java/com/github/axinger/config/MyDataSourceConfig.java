package com.github.axinger.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class MyDataSourceConfig {


    @Autowired
    private DataSourceProperties dataSourceProperties;


    protected DataSource createDataSource(String key) {
        DataSourceProperties.DataSourceItem item = dataSourceProperties.getDatasource().get(key);
        if (item == null) {
            throw new IllegalArgumentException("No configuration found for data source: " + key);
        }

        return DataSourceBuilder.create().type(HikariDataSource.class).url(item.getUrl()).username(item.getUsername()).password(item.getPassword()).driverClassName(item.getDriverClassName()).build();
    }


    @Configuration
    @EnableConfigurationProperties
    @MapperScan(basePackages = {"com.github.db1.mapper"}, sqlSessionTemplateRef = "masterSqlSessionTemplate")
    public static class MasterDataSourceConfig extends MyDataSourceConfig {
        @Primary
        @Bean(name = "masterDataSource")
        public DataSource masterDataSource() {
            return this.createDataSource("master");
        }

        @Bean(name = "masterSqlSessionFactory")
        @Primary
        public SqlSessionFactory dbSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
            MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
            factoryBean.setDataSource(dataSource);
            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                    .getResources("classpath*:/mapper/db1/*.xml"));
            return factoryBean.getObject();
        }

        @Bean(name = "masterTransactionManager")
        @Primary
        public DataSourceTransactionManager dbTransactionManager(@Qualifier("masterDataSource") DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean(name = "masterSqlSessionTemplate")
        @Primary
        public SqlSessionTemplate dbSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
            return new SqlSessionTemplate(sqlSessionFactory);
        }

    }

    @Configuration
    @EnableConfigurationProperties
    @MapperScan(basePackages = {"com.github.db2.mapper", "com.github.db21.mapper"}, sqlSessionFactoryRef = "slaveSqlSessionFactory")
    public static class SlaveDataSourceConfig extends MyDataSourceConfig {

        @Bean(name = "slaveDataSource")
        public DataSource slaveDataSource() {
            return this.createDataSource("slave");
        }

        @Bean(name = "slaveSqlSessionFactory")
        public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource slaveDataSource) throws Exception {
            MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
            sessionFactory.setDataSource(slaveDataSource);
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            List<String> list = List.of(
                    "classpath*:mapper/db2/*.xml",
                    "classpath*:mapper/db21/*.xml"
            );
            List<Resource> resources = new ArrayList<>();
            for (String path : list) {
                Resource[] mappers = resolver.getResources(path);
                resources.addAll(Arrays.asList(mappers));
            }
            sessionFactory.setMapperLocations(resources.toArray(new Resource[0]));
            return sessionFactory.getObject();
        }


        @Bean(name = "slaveSqlSessionTemplate")
        public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
            return new SqlSessionTemplate(sqlSessionFactory);
        }

        @Bean(name = "slaveTransactionManager")
        public PlatformTransactionManager slaveTransactionManager(@Qualifier("slaveDataSource") DataSource slaveDataSource) {
            return new DataSourceTransactionManager(slaveDataSource);
        }
    }


}

