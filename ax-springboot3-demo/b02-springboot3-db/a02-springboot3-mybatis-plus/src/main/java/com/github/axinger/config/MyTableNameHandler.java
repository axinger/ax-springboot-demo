package com.github.axinger.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

public class MyTableNameHandler implements TableNameHandler {

    //每个请求线程维护一个数据，避免多线程数据冲突。所以使用ThreadLocal
    private static final ThreadLocal<String> SUFFIX = new ThreadLocal<>();

    //设置请求线程的month数据
    public static void setData(String suffix) {
        SUFFIX.set(suffix);
    }

    @Override
    public String dynamicTableName(String sql, String tableName) {
        System.err.println("sql：" + sql);
        String suffix = SUFFIX.get();
        if (StrUtil.isNotBlank(suffix)) {
            SUFFIX.remove();
            System.err.println("suffix：" + suffix);
            System.err.println("动态查询表：" + tableName + suffix);
            return tableName + suffix;
        }
        return tableName;
    }
}
