package com.github.axinger;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class JdbcTests {


    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Test
    void test10() {

        // 拼接 SQL 查询语句
        String sql = "SELECT a, c, b, d FROM sys_alphabet";

        // 执行查询并返回结果
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        System.out.println("maps = " + maps);

        // 拼接 SQL 查询语句
        String sql2 = "SELECT * FROM sys_alphabet";

        // 执行查询并返回结果
        List<Map<String, Object>> maps2 = jdbcTemplate.queryForList(sql2);
        System.out.println("maps2 = " + maps2);
    }

    @Test
    void test101() {
        String sql = "SELECT a, c, b, d FROM sys_alphabet where id = ?";
        Map<String, Object> stu = jdbcTemplate.queryForMap(sql, 1);
        System.out.println(stu);
    }

    @Test
    void test1011() {
        String sql = "SELECT * FROM sys_employee where id = ?";
        Map<String, Object> stu = jdbcTemplate.queryForMap(sql, 1);
        System.out.println(stu);
    }

    @Test
    void test102() {
        ///  :方式动态别名
        String sql = "SELECT a as :AA, c, b, d FROM sys_alphabet where id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", "1");
        params.put("AA", "大A");

        Map<String, Object> stu = namedParameterJdbcTemplate.queryForMap(sql, params);
        System.out.println("stu = " + stu);
    }

    @Test
    void test103() {
// 先查询
        String selectSql = "SELECT * FROM sys_alphabet WHERE id = 1";
        Map<String, Object> dataToDelete = jdbcTemplate.queryForMap(selectSql);
// 再删除
        String deleteSql = "DELETE FROM sys_alphabet WHERE id = 1";
        int affectedRows = jdbcTemplate.update(deleteSql);
        System.out.println("被删除的数据: " + dataToDelete);
        System.out.println("删除了 " + affectedRows + " 行");
    }

    @Test
    void test104() {
// 先查询
        String selectSql = "SELECT * FROM sys_alphabet WHERE id = 1";
        Map<String, Object> dataToDelete = jdbcTemplate.queryForMap(selectSql);

        String sql = "UPDATE sys_alphabet SET a=:a WHERE id=:id";

        Map<String, Object> params = new HashMap<>();
        params.put("a", "王五");
        params.put("id", "1");

        int affectedRows = namedParameterJdbcTemplate.update(sql, params);
        System.out.println("更新了 " + affectedRows + " 行");
    }

    @Test
    void test105() {
        String sql = "INSERT INTO sys_alphabet (a, b) VALUES (?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{"张三", 20});
        batchArgs.add(new Object[]{"李四", 25});
        batchArgs.add(new Object[]{"王五", 30});

        int[] affectedRows = jdbcTemplate.batchUpdate(sql, batchArgs);
        System.out.println("批量插入了 " + affectedRows.length + " 条数据");

    }

    @Test
    void test11() {
        StringBuilder sql = new StringBuilder("SELECT a, c, b, d FROM sys_alphabet WHERE 1=1");
        Map<String, Object> params = new HashMap<>();
        params.put("a", "jim");

        // 动态拼接条件
        if (params.containsKey("a")) {
            sql.append(" AND a = :a");
        }
        if (params.containsKey("c")) {
            sql.append(" AND c = :c");
        }

        List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(sql.toString(), params);
        System.out.println("maps = " + maps);

        maps.forEach(val -> val.replaceAll((key, value) -> {
            if (value instanceof LocalDateTime) {
                return LocalDateTimeUtil.format((LocalDateTime) value, "yyyy-MM-dd HH:mm:ss");
            } else if (value instanceof Timestamp) { //Timestamp extends java.util.Date
                return DateUtil.format((Timestamp) value, "yyyy-MM-dd HH:mm:ss");
            } else if (value == null) {
                return "-";//自定义null返回值
            }
            return value;
        }));
        System.out.println("修改值后data2 = " + maps);
    }

    @Test
    void test12() {
        StringBuilder sql = new StringBuilder("SELECT a, c, b, d FROM sys_alphabet");
        Map<String, Object> params = new HashMap<>();
        params.put("a", "jim");


        List<String> conditions = new ArrayList<>();
        Map<String, Object> paramMap = new HashMap<>();

        // 动态拼接条件
        if (params.containsKey("a")) {
            conditions.add("a = :a");
            paramMap.put("a", params.get("a"));
        }
        if (params.containsKey("c")) {
            conditions.add("c = :c");
            paramMap.put("c", params.get("c"));
        }

        // 合并条件到 SQL
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", conditions));
        }

        List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(sql.toString(), paramMap);
        System.out.println("maps = " + maps);

        maps.forEach(val -> val.replaceAll((key, value) -> {
            if (value instanceof LocalDateTime) {
                return LocalDateTimeUtil.format((LocalDateTime) value, "yyyy-MM-dd HH:mm:ss");
            } else if (value instanceof Timestamp) { //Timestamp extends java.util.Date
                return DateUtil.format((Timestamp) value, "yyyy-MM-dd HH:mm:ss");
            } else if (value == null) {
                return "-";//自定义null返回值
            }
            return value;
        }));
        System.out.println("修改值后data2 = " + maps);
    }

}
