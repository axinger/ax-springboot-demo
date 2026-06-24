package com.github.axinger.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.experimental.UtilityClass;

import java.util.Date;

import static com.github.axinger.util.SFunctionUtil.columnToString;

/**
 * MyBatis-Plus SQL 方法扩展工具类（配合 Lombok @ExtensionMethod 使用）
 * <p>
 * 使用方式：在类上添加注解
 * <pre>
 *   @ExtensionMethod(MyBatisPlusSQLExtensionMethodUtil.class)
 *   public class XxxTest { ... }
 * </pre>
 */
@UtilityClass
public class MyBatisPlusSQLExtensionMethodUtil {

    // ==================== MATCH AGAINST 全文检索 ====================

    /**
     * 全文检索匹配（双列）
     * SQL：MATCH(column1, column2) AGAINST ({val})
     *
     * @param wrapper   查询链包装器
     * @param condition 是否执行匹配操作的条件
     * @param column1   要匹配的列
     * @param column2   要匹配的列
     * @param val       匹配的值
     * @param <T>       泛型类型
     * @return 查询链包装器
     */
    public static <T> LambdaQueryChainWrapper<T> $matchAgainst(LambdaQueryChainWrapper<T> wrapper, boolean condition,
                                                              SFunction<T, ?> column1, SFunction<T, ?> column2, String val) {
        if (condition) {
            wrapper.apply("MATCH(" + columnToString(column1) + "," + columnToString(column2) + ") AGAINST ({0})", val);
        }
        return wrapper;
    }

    /**
     * 全文检索匹配（单列）
     * SQL：MATCH(column) AGAINST ({val})
     *
     * @param wrapper   查询链包装器
     * @param condition 是否执行匹配操作的条件
     * @param column    要匹配的列
     * @param val       匹配的值
     * @param <T>       泛型类型
     * @return 查询链包装器
     */
    public static <T> LambdaQueryChainWrapper<T> $matchAgainst(LambdaQueryChainWrapper<T> wrapper, boolean condition,
                                                              SFunction<T, ?> column, String val) {
        if (condition) {
            wrapper.apply("MATCH(" + columnToString(column) + ") AGAINST ({0})", val);
        }
        return wrapper;
    }

    /**
     * 全文检索匹配（双列，无条件）
     * SQL：MATCH(column1, column2) AGAINST ({val})
     *
     * @param wrapper 查询链包装器
     * @param column1 要匹配的列
     * @param column2 要匹配的列
     * @param val     匹配的值
     * @param <T>     泛型类型
     * @return 查询链包装器
     */
    public static <T> LambdaQueryChainWrapper<T> $matchAgainst(LambdaQueryChainWrapper<T> wrapper,
                                                              SFunction<T, ?> column1, SFunction<T, ?> column2, String val) {
        return $matchAgainst(wrapper, true, column1, column2, val);
    }

    /**
     * 全文检索匹配（单列，无条件）
     * SQL：MATCH(column) AGAINST ({val})
     *
     * @param wrapper 查询链包装器
     * @param column  要匹配的列
     * @param val     匹配的值
     * @param <T>     泛型类型
     * @return 查询链包装器
     */
    public static <T> LambdaQueryChainWrapper<T> $matchAgainst(LambdaQueryChainWrapper<T> wrapper,
                                                              SFunction<T, ?> column, String val) {
        return $matchAgainst(wrapper, true, column, val);
    }

    // ==================== LambdaQueryWrapper 重载 ====================

    /**
     * 全文检索匹配（LambdaQueryWrapper 双列）
     */
    public static <T> LambdaQueryWrapper<T> $matchAgainst(LambdaQueryWrapper<T> wrapper, boolean condition,
                                                            SFunction<T, ?> column1, SFunction<T, ?> column2, String val) {
        if (condition) {
            wrapper.apply("MATCH(" + columnToString(column1) + "," + columnToString(column2) + ") AGAINST ({0})", val);
        }
        return wrapper;
    }

    /**
     * 全文检索匹配（LambdaQueryWrapper 单列）
     */
    public static <T> LambdaQueryWrapper<T> $matchAgainst(LambdaQueryWrapper<T> wrapper, boolean condition,
                                                            SFunction<T, ?> column, String val) {
        if (condition) {
            wrapper.apply("MATCH(" + columnToString(column) + ") AGAINST ({0})", val);
        }
        return wrapper;
    }

    /**
     * 全文检索匹配（LambdaQueryWrapper 双列，无条件）
     */
    public static <T> LambdaQueryWrapper<T> $matchAgainst(LambdaQueryWrapper<T> wrapper,
                                                            SFunction<T, ?> column1, SFunction<T, ?> column2, String val) {
        return $matchAgainst(wrapper, true, column1, column2, val);
    }

    /**
     * 全文检索匹配（LambdaQueryWrapper 单列，无条件）
     */
    public static <T> LambdaQueryWrapper<T> $matchAgainst(LambdaQueryWrapper<T> wrapper,
                                                            SFunction<T, ?> column, String val) {
        return $matchAgainst(wrapper, true, column, val);
    }

    // ==================== LIMIT 分页限制 ====================

    /**
     * 对查询链包装器进行 LIMIT 限制
     *
     * @param wrapper 查询链包装器
     * @param num     限制数量
     * @param <T>     泛型类型
     * @return 查询链包装器
     */
    public static <T> LambdaQueryChainWrapper<T> $limit(LambdaQueryChainWrapper<T> wrapper, Integer num) {
        return wrapper.last("LIMIT " + num);
    }

    /**
     * 对 LambdaQueryWrapper 进行 LIMIT 限制
     */
    public static <T> LambdaQueryWrapper<T> $limit(LambdaQueryWrapper<T> wrapper, Integer num) {
        return wrapper.last("LIMIT " + num);
    }

    // ==================== 日期范围查询 ====================

    /**
     * 时间范围查询（Date 类型）
     *
     * @param wrapper   查询链包装器
     * @param column    时间列
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param <T>       泛型类型
     * @return 查询链包装器
     */
    public static <T> LambdaQueryChainWrapper<T> $rangeDateTime(LambdaQueryChainWrapper<T> wrapper,
                                                                SFunction<T, ?> column, Date startTime, Date endTime) {
        return wrapper.ge(startTime != null, column, startTime)
                .le(endTime != null, column, endTime);
    }

    /**
     * 时间范围查询（LambdaQueryWrapper）
     */
    public static <T> LambdaQueryWrapper<T> $rangeDateTime(LambdaQueryWrapper<T> wrapper,
                                                            SFunction<T, ?> column, Date startTime, Date endTime) {
        return wrapper.ge(startTime != null, column, startTime)
                .le(endTime != null, column, endTime);
    }
}
