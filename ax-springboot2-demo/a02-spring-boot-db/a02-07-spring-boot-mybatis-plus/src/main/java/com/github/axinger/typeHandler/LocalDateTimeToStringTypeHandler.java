package com.github.axinger.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedTypes(value = {LocalDateTime.class, Timestamp.class})
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class LocalDateTimeToStringTypeHandler extends BaseTypeHandler<String> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        // 反向转换逻辑（按需实现）
        // 将 String 类型的日期时间字符串转换为 LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(parameter, FORMATTER);
        // 将 LocalDateTime 转换为 Timestamp
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        // 设置 PreparedStatement 的参数
        ps.setTimestamp(i, timestamp);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        LocalDateTime dateTime = rs.getObject(columnName, LocalDateTime.class);
        return dateTime != null ? FORMATTER.format(dateTime) : null;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        LocalDateTime dateTime = rs.getObject(columnIndex, LocalDateTime.class);
        return dateTime != null ? FORMATTER.format(dateTime) : null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        LocalDateTime dateTime = cs.getObject(columnIndex, LocalDateTime.class);
        return dateTime != null ? FORMATTER.format(dateTime) : null;
    }
}
