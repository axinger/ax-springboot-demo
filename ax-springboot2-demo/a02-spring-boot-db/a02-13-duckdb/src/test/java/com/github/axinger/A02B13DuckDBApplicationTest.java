package com.github.axinger;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ConsoleTable;
import com.github.axinger.mapper.DuckDBMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class A02B13DuckDBApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private DuckDBMapper duckDBMapper;

    @Test
    public void test1() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String sql = "SELECT * FROM read_csv(?)";
        List<Map<String, Object>> maps1 = jdbcTemplate.queryForList(sql, "./opt/123.csv");
        System.out.println("maps1 = " + maps1);


        ///  表名不能动态拼接
        List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(String.format("SELECT * FROM '%s' where id=:id", "./opt/123.csv"), Map.of("id", 2));
        System.out.println("maps = " + maps);

        String sql2 = """
                    select * from './opt/123.csv'
                """;
        List<Map<String, Object>> maps2 = jdbcTemplate.queryForList(sql2);
        System.out.println("maps2 = " + maps2);

        String sql3 = """
                    select * from './opt/123.json'
                """;
        List<Map<String, Object>> maps3 = jdbcTemplate.queryForList(sql3);
        System.out.println("maps3 = " + maps3);


        String sql4 = """
                    select * from './opt/*.json'
                """;
        List<Map<String, Object>> maps4 = jdbcTemplate.queryForList(sql4);
        System.out.println("maps4 = " + maps4);


        /// 不能修改文件,可以读取,再写入
        String sql5 = """
                select t1.*,t2.*
                      from './opt/123.json' t1
                      left join './opt/1234.json' t2 on t1.name=t2.name
                """;
        List<Map<String, Object>> maps5 = jdbcTemplate.queryForList(sql5);
        System.out.println("maps5 = " + maps5);


        String sql6 = """
                    select id,
                              name,
                              age + 1 AS age,
                              'Engineer' AS job  from './opt/123.json'
                """;
        List<Map<String, Object>> maps6 = jdbcTemplate.queryForList(sql6);
        System.out.println("maps6 = " + maps6);


        /*
            {"id":1,"name":"jim","age":11}
            {"id":2,"name":"tom","age":21}
         */
        String sql7 = """
                COPY (
                  SELECT id, name, age + 1 AS age
                  FROM './opt/123.json'
                ) TO './out/new_123_1.json' WITH (FORMAT JSON, ARRAY false);
                """;
        jdbcTemplate.execute(sql7);

        /*
            [
            {"id":1,"name":"jim","age":11},
            {"id":2,"name":"tom","age":21}
            ]
         */
        String sql8 = """
                COPY (
                  SELECT id, name, age + 1 AS age
                  FROM read_json('./opt/123.json')
                ) TO './out/new_123_2.json' WITH (FORMAT JSON, ARRAY true);
                """;
        jdbcTemplate.execute(sql8);


        /*
        可以自定义分隔符：DELIMITER ';' 或 DELIMITER '|'
         */
        String sql9 = """
                COPY (
                  SELECT id, name, age + 1 AS age
                  FROM './opt/123.csv'
                ) TO './out/new_123_1.csv' WITH (FORMAT CSV, HEADER true,DELIMITER '|');
                """;
        jdbcTemplate.execute(sql9);


        /*
        支持压缩：COMPRESSION GZIP（生成 .csv.gz） --- 变成16进制了
         */
        String sql10 = """
                COPY (
                  SELECT id, name, age + 1 AS age
                  FROM './opt/123.csv'
                ) TO './out/new_123_10.csv' WITH (FORMAT CSV, HEADER true,COMPRESSION GZIP);
                """;
        jdbcTemplate.execute(sql10);


    }

    //https://blog.csdn.net/neweastsun/article/details/144523155
    // https://duckdb.org/docs/stable/guides/file_formats/excel_import
    @Test
    public void test1_excel() {
        /// 1.默认读取第一个
        String sql3 = """
                    select * from './opt/地区销售统计.xlsx'
                """;
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql3);
        System.out.println("list1 = " + list1);

        /// SELECT * FROM read_xlsx('test_excel.xlsx');
        /// SELECT * FROM read_xlsx('test_excel.xlsx', range = 'A1:B2');
        /// SELECT * FROM read_xlsx('test_excel.xlsx', sheet = 'Sheet1');
        /// ? 不用 '?'
        String sql = "SELECT * FROM read_xlsx(?,sheet =?)";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql, "./opt/地区销售统计.xlsx", "销售总额");
        System.out.println("list2 = " + list2);


//        String sql3 = "SELECT * FROM read_xlsx(?,sheet =?)";
//        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql, "./opt/地区销售统计.xlsx","销售总额");
//        System.out.println("list2 = " + list2);
    }

    @Test
    public void test1_csv() {

        String sql = "SELECT * FROM read_csv(?,delim = ',')";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql, "./opt/cities.csv");
        System.out.println("list1 = " + list1);


        List<Map<String, Object>> objects = duckDBMapper.readCsv("./opt/cities.csv");
        System.out.println("objects = " + objects);

        String sql2 = """
                 PIVOT read_csv('./opt/cities.csv',delim = ',')
                ON year
                USING sum(population);
                """;
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("list2 = " + list2);

    }

    @Test
    public void test_透视() {

//        {
//            String filename = "./opt/cities.csv";
//            String sql3 = String.format("""
//                    PIVOT read_csv('%s', delim = ',')
//                    ON year
//                    USING sum(population)
//                    """, filename.replace("'", "''"));  // 处理单引号转义
//            List<Map<String, Object>> objects = jdbcTemplate.queryForList(sql3);
//            System.out.println("objects = " + objects);
//            ConsoleTable consoleTable = ConsoleTable.create();
//            String[] titles = objects.getFirst().keySet().toArray(new String[0]);
//            consoleTable.addHeader(titles);
//            for (Map<String, Object> map : objects) {
//                Object[] array = map.values().toArray(new Object[0]);
//                String[] stringArray = Arrays.stream(array)
//                        .map(obj -> obj == null ? "" : obj.toString())
//                        .toArray(String[]::new);
//                consoleTable.addBody(stringArray);
//            }
//            Console.table(consoleTable);
//        }

        {
            String path = "./opt/cities.csv".replace("'", "''");  // 处理单引号转义
            String sql3 = String.format("""
                    PIVOT read_csv('%s', delim = ',')
                    ON year
                    USING sum(population)
                    """, path);
            List<Map<String, Object>> objects = duckDBMapper.select(sql3);
            System.out.println("拼接字符串方式objects = " + objects);
            ConsoleTable consoleTable = ConsoleTable.create();
            String[] titles = objects.getFirst().keySet().toArray(new String[0]);
            consoleTable.addHeader(titles);
            for (Map<String, Object> map : objects) {
                Object[] array = map.values().toArray(new Object[0]);
                String[] stringArray = Arrays.stream(array)
                        .map(obj -> obj == null ? "" : obj.toString())
                        .toArray(String[]::new);
                consoleTable.addBody(stringArray);
            }
            Console.table(consoleTable);
        }


        // 不行,  PIVOT read_csv(:filename 不能填充
//        String sql4 = """
//                PIVOT read_csv(:filename, delim = ',')
//                ON year
//                USING sum(population)
//                """;
//
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("filename", "./opt/cities.csv");
//        List<Map<String, Object>> list4 = namedParameterJdbcTemplate.queryForList(sql4, params);
//        System.out.println("list4 = " + list4);


        // 不行
//        String sql5 = """
//                WITH file_data AS (
//                    SELECT * FROM read_csv(?, delim = ',')
//                )
//                PIVOT file_data
//                ON year
//                USING sum(population)
//                """;
//
//        List<Map<String, Object>> list5 = jdbcTemplate.queryForList(sql5, "./opt/cities.csv");
//        System.out.println("list5 = " + list5);
        {

            List<Map<String, Object>> objects = duckDBMapper.pivot("./opt/cities.csv");
            System.out.println("objects = " + objects);

            ConsoleTable consoleTable = ConsoleTable.create();
            String[] titles = objects.getFirst().keySet().toArray(new String[0]);
            consoleTable.addHeader(titles);
            for (Map<String, Object> map : objects) {
                Object[] array = map.values().toArray(new Object[0]);
                String[] stringArray = Arrays.stream(array)
                        .map(obj -> obj == null ? "" : obj.toString())
                        .toArray(String[]::new);
                consoleTable.addBody(stringArray);
            }
            Console.table(consoleTable);
        }

        {
            List<Map<String, Object>> objects = duckDBMapper.pivotGroupByCountry("./opt/cities.csv");
            System.out.println("objects = " + objects);

            ConsoleTable consoleTable = ConsoleTable.create();
            String[] titles = objects.getFirst().keySet().toArray(new String[0]);
            consoleTable.addHeader(titles);
            for (Map<String, Object> map : objects) {
                Object[] array = map.values().toArray(new Object[0]);
                String[] stringArray = Arrays.stream(array)
                        .map(obj -> obj == null ? "" : obj.toString())
                        .toArray(String[]::new);
                consoleTable.addBody(stringArray);
            }
            Console.table(consoleTable);
        }


    }
}
