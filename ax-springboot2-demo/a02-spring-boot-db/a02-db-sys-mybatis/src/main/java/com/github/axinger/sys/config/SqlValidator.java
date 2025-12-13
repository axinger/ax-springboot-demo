package com.github.axinger.sys.config;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
public class SqlValidator {
    private static final Set<String> ALLOWED_TABLES = Set.of(
            "ods_cargo_t_cnf_function_switch_config",
            "other_allowed_table"
    );

    private static final Pattern SELECT_PATTERN = Pattern.compile("^SELECT\\s+.+\\s+FROM\\s+([\\w_]+)", Pattern.CASE_INSENSITIVE);

    public static void validateSelectSql(String sql) {
        // 基础校验
        if (sql == null || sql.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL 不能为空");
        }

        // 转换为统一格式便于检查
        String unified = sql.toUpperCase().replaceAll("\\s+", " ");

        // 必须是以 SELECT 开头
        if (!unified.startsWith("SELECT ")) {
            throw new IllegalArgumentException("只允许 SELECT 语句");
        }

        // 解析表名
        Matcher matcher = SELECT_PATTERN.matcher(unified);
        if (!matcher.find()) {
            throw new IllegalArgumentException("SQL 格式不正确");
        }

//        String tableName = matcher.group(1).toLowerCase();
//        if (!ALLOWED_TABLES.contains(tableName)) {
//            throw new IllegalArgumentException("不允许访问表: " + tableName);
//        }

        // 可以添加更多校验规则...
    }
}
