package com.github.axinger.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MybatisPlusConfig.java
 * @createTime 2022年01月31日 00:41:00
 */

@Slf4j
@Configuration
public class MybatisPlusConfig implements MetaObjectHandler {


    @Autowired
    private TenantProperties tenantProperties;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("insertFill================================================================");
        this.strictInsertFill(metaObject, "version", Long.class, 1L);
        this.insertDateFill("createTime", metaObject);
        this.insertDateFill("updateTime", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("updateFill================================================================");
        this.updateDateFill("updateTime", metaObject);
        Object val = getFieldValByName("updateBy", metaObject);
        if (val != null) {
            //更新人
            this.updateByFill("updateBy", metaObject);
        }
    }


    // 官方已更新setFieldValByName方法为strictInsertFill或fillStrategy等
    protected void insertDateFill(String field, MetaObject metaObject) {
        this.strictInsertFill(metaObject, field, Date.class, new Date());
        this.strictInsertFill(metaObject, field, LocalDateTime.class, LocalDateTime.now());
    }

    @SuppressWarnings(value = {"all"})
    protected void updateDateFill(String field, MetaObject metaObject) {
        metaObject.setValue(field, null);
        this.strictUpdateFill(metaObject, field, Date.class, new Date());
        this.strictUpdateFill(metaObject, field, LocalDateTime.class, LocalDateTime.now());
        // this.strictUpdateFill(metaObject, field, () -> new Date(), Date.class);
        // this.strictUpdateFill(metaObject, field, () -> LocalDateTime.now(), LocalDateTime.class);
    }

    @SuppressWarnings(value = {"all"})
    protected void updateByFill(String updateBy, MetaObject metaObject) {
        metaObject.setValue(updateBy, null);
        this.strictUpdateFill(metaObject, updateBy, String.class, "");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 防止全表更新与删除
        // 针对 update 和 delete 语句
//        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        //1.添加动态表名插件,分页插件导致动态表名失效，调整一下插件的添加顺序，优先添加动态表名插件
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor());

        // 2.添加分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor());

        //3.乐观锁插件
        interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());

        // 4.多租户
//        if (tenantProperties.getEnable()) {
//            interceptor.addInnerInterceptor(tenantLineInnerInterceptor());
//        }
        return interceptor;
    }


    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
        return new OptimisticLockerInnerInterceptor();
    }

    //动态表名插件
    @Bean
    public DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
        DynamicTableNameInnerInterceptor interceptor = new DynamicTableNameInnerInterceptor();
        interceptor.setTableNameHandler(new MyTableNameHandler());
        return interceptor;
    }


    @Bean
    public PaginationInnerInterceptor paginationInnerInterceptor() {
        PaginationInnerInterceptor pageInterceptor = new PaginationInnerInterceptor();
        // 设置数据库方言类型
        pageInterceptor.setDbType(DbType.MYSQL);
        // 下面配置根据需求自行设置
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求。默认false
        pageInterceptor.setOverflow(false);
        // 单页分页条数限制，默认无限制
        pageInterceptor.setMaxLimit(500L);
        return pageInterceptor;
    }


    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        TenantLineInnerInterceptor interceptor = new TenantLineInnerInterceptor();
        interceptor.setTenantLineHandler(new MyTenantLineHandler(this.tenantProperties));
        return interceptor;
    }

}
