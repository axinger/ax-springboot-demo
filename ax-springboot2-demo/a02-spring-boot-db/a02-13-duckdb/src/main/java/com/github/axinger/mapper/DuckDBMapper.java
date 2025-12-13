package com.github.axinger.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DuckDBMapper {

    //  #{sql} 不行
    @Select("""
                ${sql}
            """)
    List<Map<String, Object>> select(@Param("sql") String sql);

    @Select("""
            SELECT * FROM read_csv(#{path},delim = ',')
            """)
    List<Map<String, Object>> readCsv(@Param("path") String path);

    /**
     * 透视
     * 第一个查询以year列为中心。这意味着我们将分别获得国家、姓名和年份的列。
     * ON year IN (2020,2021) ,必须加年份 ,不然报错, 正常情况是不需要加的
     */
        /*
        PIVOT cities
        ON year
        USING sum(population);

        */
    @Select("""
               PIVOT read_csv(#{path}, delim = ',')
                ON year IN (2000,2010,2020)
                USING sum(population)
            """)
    List<Map<String, Object>> pivot(@Param("path") String path);

    /**
     * 下面示例，我们再次以年份为中心，但是按国家分组的：
     */
/*
PIVOT cities
ON year
USING sum(population)
GROUP BY country;
*/
    @Select("""
            PIVOT read_csv(#{path}, delim = ',')
            ON year IN (2000,2010,2020)
            USING sum(population)
            GROUP BY country;
            """)
    List<Map<String, Object>> pivotGroupByCountry(@Param("path") String path);
}
