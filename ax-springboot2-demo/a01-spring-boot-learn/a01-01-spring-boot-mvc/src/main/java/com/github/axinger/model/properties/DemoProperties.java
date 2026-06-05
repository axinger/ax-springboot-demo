package com.github.axinger.model.properties;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * ConfigurationProperties 大合集演示类
 * 统一前缀 demo，覆盖基础类型、List、Map、嵌套对象、日期时间、YAML 特殊语法
 * 同时带有 @Component 演示自动注册 Bean
 */
@Data
@Component
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {

    // === ApplicationInfo ===
    private String version;
    private String description;

    // === AppProperties ===
    private boolean featureEnabled;
    private String featureMode;
    private List<String> featureList;

    // === AxingerPersonProperties 基础字段 ===
    private Long personId;
    private String personName;
    private Integer personAge;

    // java.util.Date
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date createdTime;

    // LocalDateTime
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime updatedTime;

    // String 时间
    private String time;
    private String time2;

    // 文本块标量对比
    private String text1;
    private String text2;
    private String text3;

    // 布尔值
    private Boolean enabled;
    private Boolean disabled;

    // null 值
    private Object empty;
    private Object empty2;

    // List
    private List<String> fruits;

    // 类型标签
    private String number;
    private Date date;

    // LocalDate
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private List<String> holidays;

    // 嵌套对象
    private Vacation vacation;

    // LocalDateTime
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // LocalTime
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime workStartTime;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime workEndTime;

    // === SysUser（@Component 演示）===
    private String sysName;
    private Integer sysAge;

    // === HumitureRuleProperties ===
    private Rule humidity;
    private Rule temperature;

    // ==================== 多级嵌套对象（二级/三级自定义对象）====================

    /**
     * 三级嵌套对象：公司 -> 部门 -> 团队 -> 成员列表
     */
    private Company company;

    /**
     * Map 嵌套 List<对象>：按分组存储成员列表
     */
    private java.util.Map<String, List<Member>> groupMembers;

    /**
     * List 嵌套 Map：元数据列表，每项是一个键值对 Map
     */
    private List<java.util.Map<String, String>> metadataList;

    /**
     * Map 嵌套 Map：双层嵌套 Map 结构
     */
    private java.util.Map<String, java.util.Map<String, String>> nestedMap;

    // ==================== 内部静态类定义 ====================

    @Data
    public static class Vacation {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate from;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate to;
    }

    @Data
    public static class Rule {
        private double min;
        private double max;
    }

    @Data
    public static class Company {
        private String name;
        private Department department;
    }

    @Data
    public static class Department {
        private String name;
        private Team team;
    }

    @Data
    public static class Team {
        private String name;
        private List<Member> members;
    }

    @Data
    public static class Member {
        private String name;
        private Integer age;
    }
}
