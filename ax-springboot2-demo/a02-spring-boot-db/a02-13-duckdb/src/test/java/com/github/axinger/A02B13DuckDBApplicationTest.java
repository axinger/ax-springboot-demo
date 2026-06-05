package com.github.axinger;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ConsoleTable;
import com.github.axinger.mapper.DuckDBMapper;
import org.junit.jupiter.api.DisplayName;
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

    // ==================== DuckDB 高级特性示例 ====================

    /**
     * DESCRIBE / SUMMARIZE：快速探查数据结构
     * 使用场景：
     * - 拿到一个陌生 CSV/JSON/Parquet 文件，想快速知道有哪些字段、类型、是否可空
     * - 做数据质量分析，一键获取每列的 count、min、max、avg、std、q25、q75 等统计信息
     * - 数据入库前的预处理评估
     */
    @Test
    @DisplayName("DESCRIBE与SUMMARIZE：快速探查数据结构和统计特征")
    public void test_describe_summarize() {
        // DESCRIBE 查看文件结构（列名、类型、是否可空等）
        List<Map<String, Object>> describe = jdbcTemplate.queryForList("DESCRIBE './opt/123.json'");
        System.out.println("DESCRIBE 123.json = " + describe);

        // SUMMARIZE 一键统计分析（每列的 count、min、max、avg、std 等）
        List<Map<String, Object>> summarize = jdbcTemplate.queryForList("SUMMARIZE read_csv('./opt/cities.csv', delim = ',')");
        System.out.println("SUMMARIZE cities.csv = " + summarize);
    }

    /**
     * 窗口函数（Window Functions）：在不改变行数的前提下做分组计算
     * 使用场景：
     * - 排名需求：如销售排行榜、成绩排名（ROW_NUMBER / RANK / DENSE_RANK）
     * - 累计计算：如累计销售额、累计用户数（SUM OVER）
     * - 同环比分析：对比前一期/后一期数据（LAG / LEAD）
     * - 分组占比：计算每条记录占组内总计的比例
     */
    @Test
    @DisplayName("窗口函数：排名、累计聚合、前后行对比（LAG/LEAD）")
    public void test_window_functions() {
        // ROW_NUMBER / RANK / 累计聚合
        String sql1 = """
                SELECT country, name, year, population,
                       ROW_NUMBER() OVER (PARTITION BY country ORDER BY population DESC) AS rn,
                       RANK() OVER (PARTITION BY country ORDER BY population DESC) AS rk,
                       SUM(population) OVER (PARTITION BY country) AS country_total,
                       AVG(population) OVER (PARTITION BY country) AS country_avg
                FROM read_csv('./opt/cities.csv', delim = ',')
                """;
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println("window list1 = " + list1);

        // LAG / LEAD：前后行对比
        String sql2 = """
                SELECT country, name, year, population,
                       LAG(population, 1) OVER (PARTITION BY name ORDER BY year) AS prev_pop,
                       LEAD(population, 1) OVER (PARTITION BY name ORDER BY year) AS next_pop
                FROM read_csv('./opt/cities.csv', delim = ',')
                """;
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("window list2 = " + list2);
    }

    /**
     * LIST 聚合 + UNNEST：一维数组与行数据的互相转换
     * 使用场景：
     * - 分组后将多个值聚合成列表（如：每个分类下的所有标签）
     * - 将数组类型的列展开为多行（如：一个订单含多个商品，拆分为商品明细行）
     * - 行转列/列转行的数据清洗场景
     */
    @Test
    @DisplayName("LIST聚合与UNNEST：数组聚合和展开（行与列表互转）")
    public void test_list_unnest() {
        // LIST 聚合：将分组数据聚合成数组
        String sql1 = """
                SELECT country,
                       LIST(name ORDER BY name) AS names,
                       LIST(DISTINCT year ORDER BY year) AS years
                FROM read_csv('./opt/cities.csv', delim = ',')
                GROUP BY country
                """;
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println("LIST 聚合 = " + list1);

        // UNNEST 展开数组为行
        String sql2 = """
                SELECT country, unnest(names) AS name
                FROM (
                    SELECT country, LIST(name) AS names
                    FROM read_csv('./opt/cities.csv', delim = ',')
                    GROUP BY country
                )
                """;
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("UNNEST 展开 = " + list2);
    }

    /**
     * 统计函数（Statistical / Analytical Aggregates）：超越常规 SUM/AVG 的分析能力
     * 使用场景：
     * - 数据探索：计算中位数（quantile_cont）、众数（mode）了解数据分布
     * - 大数据去重：approx_count_distinct 用 HyperLogLog 近似计算，性能远优于 COUNT(DISTINCT)
     * - 风控/质量监控：方差（variance）、标准差（stddev_pop）衡量数据波动
     * - A/B 测试、用户行为分析中的统计推断
     */
    @Test
    @DisplayName("统计函数：分位数、众数、近似去重、方差与标准差")
    public void test_statistical_functions() {
        // 统计函数：分位数、众数、方差、标准差
        String sql = """
                SELECT quantile_cont(population, 0.5) AS median_pop,
                       mode(name) AS mode_name,
                       approx_count_distinct(name) AS approx_unique_names,
                       variance(population) AS pop_variance,
                       stddev_pop(population) AS pop_stddev
                FROM read_csv('./opt/cities.csv', delim = ',')
                """;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("统计函数 = " + list);
    }

    /**
     * STRING_AGG / GROUP_CONCAT：分组字符串拼接
     * 使用场景：
     * - 报表展示：将同一分组的多条记录合并为一行展示（如：一个客户的所有订单号）
     * - 标签系统：将多个标签拼接为逗号分隔的字符串
     * - 生成 IN 子句列表、动态 SQL 拼接的源数据
     * - 去重后拼接（DISTINCT）保证唯一值
     */
    @Test
    @DisplayName("STRING_AGG与GROUP_CONCAT：分组字符串拼接（支持排序与去重）")
    public void test_string_agg() {
        // STRING_AGG / GROUP_CONCAT：字符串聚合
        String sql = """
                SELECT country,
                       STRING_AGG(name, ', ' ORDER BY name) AS name_list,
                       GROUP_CONCAT(DISTINCT CAST(year AS VARCHAR), ' | ') AS year_list
                FROM read_csv('./opt/cities.csv', delim = ',')
                GROUP BY country
                """;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("STRING_AGG = " + list);
    }

    /**
     * SAMPLE：随机采样，快速获取数据子集
     * 使用场景：
     * - 大数据量预览：billions 行数据中快速抽取样本查看内容
     * - 机器学习：从全量数据中随机划分训练集/测试集
     * - 数据质量抽查：随机采样验证数据正确性
     * - 性能测试：用采样数据替代全量进行 SQL 调优
     */
    @Test
    @DisplayName("SAMPLE随机采样：百分比采样与固定行数采样")
    public void test_sample() {
        // SAMPLE 随机采样：百分比
        String sql1 = "SELECT * FROM read_csv('./opt/cities.csv', delim = ',') USING SAMPLE 50%";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println("50% 采样 = " + list1);

        // 固定行数采样
        String sql2 = "SELECT * FROM read_csv('./opt/cities.csv', delim = ',') USING SAMPLE 2 ROWS";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("2行采样 = " + list2);
    }

    /**
     * SELECT * EXCLUDE / REPLACE：灵活控制 SELECT * 的输出列
     * 使用场景：
     * - EXCLUDE：排除敏感字段（如密码、身份证）或冗余大字段（如 blob、长文本）
     * - REPLACE：对个别列做轻量转换（如 +10、格式化日期），同时保留其余所有列
     * - 避免手写大量列名，提升 SQL 可维护性
     */
    @Test
    @DisplayName("SELECT * EXCLUDE/REPLACE：排除或替换指定列")
    public void test_exclude_replace() {
        // SELECT * EXCLUDE：排除指定列
        String sql1 = "SELECT * EXCLUDE (age) FROM './opt/123.json'";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println("EXCLUDE age = " + list1);

        // SELECT * REPLACE：替换指定列的表达式
        String sql2 = "SELECT * REPLACE (age + 10 AS age) FROM './opt/123.json'";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("REPLACE age = " + list2);
    }

    /**
     * UNPIVOT：宽表转长表（PIVOT 的反向操作）
     * 使用场景：
     * - 报表数据规范化：将横向排列的月份/指标列转为纵向行，方便后续聚合分析
     * - ETL 数据清洗：从 Excel 透视表或交叉表恢复为标准关系表结构
     * - 时序数据规整：将多列时间序列数据展开为 (timestamp, value) 两列格式
     */
    @Test
    @DisplayName("UNPIVOT：宽表转长表（PIVOT的反向操作）")
    public void test_unpivot() {
        // UNPIVOT：宽表转长表（PIVOT 的反向操作）
        String sql = """
                WITH wide AS (
                    SELECT * FROM (VALUES ('A', 10, 20, 30), ('B', 15, 25, 35))
                    AS t(name, y2000, y2010, y2020)
                )
                SELECT * FROM wide
                UNPIVOT (population FOR year IN (y2000, y2010, y2020))
                """;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("UNPIVOT = " + list);
    }

    /**
     * GENERATE_SERIES：生成连续序列数据
     * 使用场景：
     * - 日期补齐：生成完整的日期序列，与业务数据 LEFT JOIN 填充缺失日期
     * - 序号生成：为报表生成行号、页码、批次号
     * - 测试数据构造：快速生成百万级测试数据
     * - 时间维度表构建：生成分钟/小时级别的时间粒度表
     */
    @Test
    @DisplayName("GENERATE_SERIES：生成整数与日期连续序列")
    public void test_generate_series() {
        // GENERATE_SERIES：生成整数序列
        String sql1 = "SELECT * FROM generate_series(1, 5) AS t(num)";
        List<Map<String, Object>> list1 = jdbcTemplate.queryForList(sql1);
        System.out.println("generate_series int = " + list1);

        // 日期序列
        String sql2 = "SELECT * FROM generate_series('2024-01-01'::DATE, '2024-01-05'::DATE, INTERVAL '1' DAY) AS t(dt)";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("generate_series date = " + list2);
    }

    /**
     * Parquet 读写：列式存储格式的高效处理
     * 使用场景：
     * - 大数据分析中间件：CSV/JSON 导入后导出为 Parquet，后续分析性能提升数倍
     * - 数据湖对接：Parquet 是 Hive、Spark、Presto 等系统的标准格式
     * - 谓词下推：只读取 WHERE 条件需要的行和 SELECT 需要的列，极大减少 I/O
     * - 列式压缩：相比 CSV 体积小 50%~90%，且保留类型信息
     */
    @Test
    @DisplayName("Parquet读写：列式存储、导出与谓词下推读取")
    public void test_parquet() {
        // 导出为 Parquet（列式存储，分析性能更好，支持谓词下推）
        String sql1 = """
                COPY (SELECT * FROM './opt/123.json')
                TO './out/123.parquet' (FORMAT PARQUET)
                """;
        jdbcTemplate.execute(sql1);

        // 读取 Parquet
        String sql2 = "SELECT * FROM read_parquet('./out/123.parquet')";
        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql2);
        System.out.println("read_parquet = " + list2);

        // Parquet 支持谓词下推，只读取需要的行和列
        String sql3 = "SELECT name, age FROM read_parquet('./out/123.parquet') WHERE age > 10";
        List<Map<String, Object>> list3 = jdbcTemplate.queryForList(sql3);
        System.out.println("parquet 过滤 = " + list3);
    }

    /**
     * ASOF JOIN：近似匹配连接（模糊时间连接）
     * 使用场景：
     * - 金融交易：将成交记录与最近一次的报价快照关联
     * - 物联网传感器：将事件日志与最近一次设备状态上报关联
     * - 日志分析：将错误日志与最近一次的部署/配置变更关联
     * - 数据补全：按时间就近原则填充缺失的维度数据
     */
    @Test
    @DisplayName("ASOF JOIN：近似时间匹配连接（金融/IoT/日志场景）")
    public void test_asof_join() {
        // ASOF JOIN：按最近时间/序号匹配（常用于金融时间序列、传感器数据）
        String sql = """
                WITH prices AS (
                    SELECT * FROM (VALUES (1, 100), (5, 101), (10, 102))
                    AS t(ts, price)
                ), trades AS (
                    SELECT * FROM (VALUES (2, 10), (7, 20))
                    AS t(ts, amount)
                )
                SELECT trades.ts, trades.amount, prices.price
                FROM trades
                ASOF LEFT JOIN prices ON trades.ts >= prices.ts
                ORDER BY trades.ts
                """;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("ASOF JOIN = " + list);
    }

    /**
     * 递归 CTE（Recursive CTE）：处理层级/树形/图结构数据
     * 使用场景：
     * - 组织架构：查询某员工的所有下属（层级递归）
     * - 菜单/类目树：生成面包屑导航路径
     * - 网络路由：查找最短路径或可达节点
     * - 数列生成：斐波那契、等差/等比数列
     */
    @Test
    @DisplayName("递归CTE：层级数据遍历与数列生成")
    public void test_recursive_cte() {
        // 递归 CTE：生成层级数据（如斐波那契数列）
        String sql = """
                WITH RECURSIVE fib(a, b) AS (
                    SELECT 0, 1
                    UNION ALL
                    SELECT b, a + b FROM fib WHERE b < 100
                )
                SELECT a AS fib_num FROM fib
                """;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("递归CTE fib = " + list);
    }

    /**
     * 实用标量函数：COALESCE、NULLIF、GREATEST、LEAST 等
     * 使用场景：
     * - COALESCE：字段回退，如优先取实际姓名，不存在则取昵称，再不存在则显示"未知"
     * - NULLIF：避免除零错误，如 NULLIF(divisor, 0)
     * - GREATEST / LEAST：多字段取最大/最小值，如比较多个渠道的价格
     * - CASE WHEN：条件分支，如年龄分段、状态映射
     */
    @Test
    @DisplayName("实用标量函数：COALESCE回退、NULLIF防除零、GREATEST/LEAST多值比较")
    public void test_practical_functions() {
        // 实用标量函数
        String sql = """
                SELECT id, name, age,
                       COALESCE(NULL, name, 'unknown') AS coalesce_demo,
                       NULLIF(age, 0) AS nullif_demo,
                       CASE WHEN age >= 18 THEN 'adult' ELSE 'minor' END AS age_group,
                       GREATEST(id, age) AS max_val,
                       LEAST(id, age) AS min_val
                FROM './opt/123.json'
                """;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        System.out.println("实用函数 = " + list);
    }
}
