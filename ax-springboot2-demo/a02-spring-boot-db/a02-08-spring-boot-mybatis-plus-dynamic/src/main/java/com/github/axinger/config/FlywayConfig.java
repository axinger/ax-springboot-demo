package com.github.axinger.config;


import cn.hutool.core.util.ObjUtil;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@Configuration
@EnableTransactionManagement
public class FlywayConfig {

    private static final String ENCODING = "UTF-8";
    //    @Autowired
//    private DataSource dataSource;
    @Resource
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    @Bean
    public void migrate() {
        log.info("开始执行数据库迁移");
        final Map<String, DataSourceProperty> map = dynamicDataSourceProperties.getDatasource();
        map.forEach((k, v) -> {
            log.info("多数据源={}，{}，{}", v.getUrl(), v.getUsername(), v.getPassword());

            String path = "";
            if (ObjUtil.equal(k, "db_1")) {
                path = "classpath:db/migration";
            } else if (ObjUtil.equal(k, "db_2")) {
                path = "classpath:db2/migration";
            }

            Flyway flyway = Flyway.configure()
                    .dataSource(v.getUrl(), v.getUsername(), v.getPassword())
                    .encoding(ENCODING)
                    .baselineOnMigrate(true)
                    .baselineVersion("0.1")  // 基线版本
                    .baselineDescription(k + "初始化")
                    .locations(path)  // sql脚本存放地址
                    .load();
            flyway.migrate();
            log.info("{} 数据库迁移完成", k);
        });
    }
}
