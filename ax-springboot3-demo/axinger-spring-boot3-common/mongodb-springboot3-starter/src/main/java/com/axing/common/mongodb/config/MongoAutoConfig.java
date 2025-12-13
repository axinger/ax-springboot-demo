package com.axing.common.mongodb.config;

import com.axing.common.mongodb.service.MongoService;
import com.axing.common.mongodb.service.impl.MongoDBServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MongoConfig.java
 * @description 去除数据库中_clas字段
 * @createTime 2022年05月06日 22:31:00
 */

@Configuration
public class MongoAutoConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory,
                                                       MongoMappingContext context) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

    @Bean
    @ConditionalOnMissingBean(MongoService.class)
    public MongoService mongoService() {
        return new MongoDBServiceImpl(mongoTemplate);
    }
}
