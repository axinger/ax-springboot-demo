//package com.github.axinger.config;
//
//import org.apache.ibatis.annotations.Param;
//
//public class DynamicSqlProvider {
//    public String buildQuery(@Param("sql") String sql) {
//        System.out.println("校验sql = " + sql);
//        SqlValidator.validateSelectSql(sql);
//        return sql;
//    }
//}
