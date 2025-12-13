package com.github.axinger.server;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdvancedCsvService {

    private final JdbcTemplate jdbcTemplate;

    public AdvancedCsvService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 读取CSV并执行复杂查询
     */
    public List<Map<String, Object>> complexQuery(String csvFilePath) {
        String sql = """
                WITH csv_data AS (
                    SELECT * FROM read_csv(?, 
                        columns={'id': 'INTEGER', 'name': 'VARCHAR', 'age': 'INTEGER', 'salary': 'DOUBLE'},
                        auto_detect=true
                    )
                )
                SELECT 
                    name,
                    age,
                    salary,
                    AVG(salary) OVER() as avg_salary
                FROM csv_data 
                WHERE age > 25
                ORDER BY salary DESC
                """;

        return jdbcTemplate.queryForList(sql, csvFilePath);
    }

    /**
     * 读取多个CSV文件
     */
    public List<Map<String, Object>> readMultipleCsvFiles(List<String> filePaths) {
        String placeholders = String.join(",", filePaths.stream()
                .map(path -> "read_csv(?)")
                .toArray(String[]::new));

        String sql = "SELECT * FROM " + placeholders;
        return jdbcTemplate.queryForList(sql, filePaths.toArray());
    }

    /**
     * 获取CSV文件结构信息
     */
    public List<Map<String, Object>> getCsvSchema(String csvFilePath) {
        String sql = """
                SELECT 
                    column_name,
                    data_type
                FROM pragma_table_info('read_csv(?)')
                """;

        return jdbcTemplate.queryForList(sql, csvFilePath);
    }
}
