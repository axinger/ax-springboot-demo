//package com.github.axinger;
//
//import com.atomikos.jdbc.AtomikosDataSourceBean;
//import com.mysql.cj.jdbc.MysqlXADataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Bean(name = "db1DataSource")
//    public DataSource db1DataSource() {
//        MysqlXADataSource mysqlXADS = new MysqlXADataSource();
//        mysqlXADS.setUrl("jdbc:mysql://hadoop102:3306/ax_test1?serverTimezone=GMT%2B8&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true");
//        mysqlXADS.setUser("root");
//        mysqlXADS.setPassword("123456");
//
//        AtomikosDataSourceBean xaDS = new AtomikosDataSourceBean();
//        xaDS.setXaDataSource(mysqlXADS);
//        xaDS.setUniqueResourceName("db1Ds");
//        return xaDS;
//    }
//
//    // 如果需要第二个数据源，可以类似地定义另一个方法
//
//}
