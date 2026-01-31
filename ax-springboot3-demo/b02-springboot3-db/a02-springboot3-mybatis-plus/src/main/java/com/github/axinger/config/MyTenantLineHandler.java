package com.github.axinger.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;

@Slf4j
public class MyTenantLineHandler implements TenantLineHandler {


    private static final ThreadLocal<String> TENANT_ID = new ThreadLocal<>();
    private final TenantProperties tenantProperties;

    public MyTenantLineHandler(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    /**
     * 获取租户ID
     *
     * @return Expression
     */
    @Override
    public Expression getTenantId() {
        String tenantId = TENANT_ID.get();
        TENANT_ID.remove();
        log.info("tenantId = {}", tenantId);

        if (tenantId != null) {
            return new StringValue(tenantId);
        } else {
            return new NullValue();
//            return new StringValue("");

        }
    }

    public static void setTenantId(String tenantId) {
        TENANT_ID.set(tenantId);
    }

    /**
     * 获取多租户的字段名
     *
     * @return String
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

    /**
     * 过滤不需要根据租户隔离的表
     * 这是 default 方法,默认返回 false 表示所有表都需要拼多租户条件
     *
     * @param tableName 表名
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return tenantProperties.getExclusionTable().stream().anyMatch(
                (t) -> t.equalsIgnoreCase(tableName)
        );
    }
}
