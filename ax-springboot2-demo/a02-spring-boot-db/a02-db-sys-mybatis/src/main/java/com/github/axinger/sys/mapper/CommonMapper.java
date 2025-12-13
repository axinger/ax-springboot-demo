package com.github.axinger.sys.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public interface CommonMapper {

    @MapKey("id")
        // 使用${}引用参数作为Map Key字段
    List<Map<String, Object>> queryTableMap(@Param("sql") String sql);


    List<LinkedHashMap<String, Object>> queryTableLinkedHashMap(@Param("sql") String sql);

    @MapKey("id")
        // resultType="java.util.LinkedHashMap",但返回值是 Map 泛型
    List<Map<String, Object>> queryTableLinkedHashMap2(@Param("sql") String sql);

    List<TreeMap<String, Object>> queryTableTreeMap(@Param("sql") String sql);


    @MapKey("${mapKey}")
//    @SelectProvider(type = DynamicSqlProvider.class, method = "buildQuery") // 不能校验
    List<Map<String, Object>> queryTableMap2(@Param("sql") String sql, @Param("mapKey") String mapKey);


    List<LinkedHashMap<String, Object>> queryTableHasId(@Param("sql") String sql, @Param("params") Map<String, Object> map);


    // 方式1：完全动态SQL（需严格校验）
//    @Insert("${sql}")
//    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRawSql(@Param("sql") String sql);


    /**
     * 通用查询方法 - 返回Map列表
     */
    /*
1. ${sql} - 字符串替换
特点：
直接替换：将参数值原样替换到SQL中

不进行转义：直接拼接SQL字符串

有SQL注入风险

2. #{sql} - 参数占位符
特点：
预编译处理：使用 ? 占位符

自动转义：防止SQL注入

类型安全：根据参数类型正确处理
     */
    @Select("${sql}")
    List<Map<String, Object>> executeQuery(@Param("sql") String sql);

    /**
     * 通用查询方法 - 返回指定类型
     */
    @Select("${sql}")
    <T> List<T> executeQuery(@Param("sql") String sql, Class<T> resultType);

    /**
     * 通用查询单条记录
     */
    @Select("${sql}")
    Map<String, Object> executeQuerySingle(@Param("sql") String sql);

    /**
     * 通用更新操作
     */
    @Update("${sql}")
    int executeUpdate(@Param("sql") String sql);
}
