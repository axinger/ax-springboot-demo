package com.axing.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MybatisPlusConfig.java
 * @Description TODO
 * @createTime 2022年01月31日 00:41:00
 */
@Configuration
@Slf4j
public class MybatisPlusConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "version", Long.class, 1L);
        this.insertDateFill("createTime", metaObject);
        this.insertDateFill("updateTime", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.updateDateFill("updateTime", metaObject);
        Object val = getFieldValByName("updateBy", metaObject);
        if (val != null) {
            this.updateDateFill("updateBy", metaObject);
        }
    }


    // 官方已更新setFieldValByName方法为strictInsertFill或fillStrategy等
    protected void insertDateFill(String field, MetaObject metaObject) {
        this.strictInsertFill(metaObject, field, Date.class, new Date());
        this.strictInsertFill(metaObject, field, LocalDateTime.class, LocalDateTime.now());
    }

    protected void updateDateFill(String field, MetaObject metaObject) {
        metaObject.setValue(field, null);
        this.strictUpdateFill(metaObject, field, Date.class, new Date());
        this.strictUpdateFill(metaObject, field, LocalDateTime.class, LocalDateTime.now());
        // this.strictUpdateFill(metaObject, field, () -> new Date(), Date.class);
        // this.strictUpdateFill(metaObject, field, () -> LocalDateTime.now(), LocalDateTime.class);
    }


    @Bean
    public MybatisPlusInterceptor optimisticLockerInnerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 乐观锁
        OptimisticLockerInnerInterceptor lockerInnerInterceptor = new OptimisticLockerInnerInterceptor();
        interceptor.addInnerInterceptor(lockerInnerInterceptor);

        // 分页
//        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        // 禁止删除数据库表中的所有内容
        // interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

}
