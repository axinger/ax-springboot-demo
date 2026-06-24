package com.github.axinger.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.experimental.UtilityClass;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SFunction 工具类：将 Lambda 表达式转换为数据库列名
 */
@UtilityClass
public class SFunctionUtil {

    private static final Map<SFunction<?, ?>, String> CACHE = new ConcurrentHashMap<>();

    /**
     * 将 SFunction 转换为下划线命名的数据库列名
     *
     * @param column Lambda 表达式，如 Article::getTitle
     * @param <T>    实体类型
     * @return 数据库列名，如 title
     */
    public static <T> String columnToString(SFunction<T, ?> column) {
        return CACHE.computeIfAbsent(column, func -> {
            LambdaMeta meta = LambdaUtils.extract(func);
            return camelToUnderline(PropertyNamer.methodToProperty(meta.getImplMethodName()));
        });
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param camel 驼峰字符串，如 createTime
     * @return 下划线字符串，如 create_time
     */
    public static String camelToUnderline(String camel) {
        if (camel == null || camel.isEmpty()) {
            return camel;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            char ch = camel.charAt(i);
            if (Character.isUpperCase(ch)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }
}
