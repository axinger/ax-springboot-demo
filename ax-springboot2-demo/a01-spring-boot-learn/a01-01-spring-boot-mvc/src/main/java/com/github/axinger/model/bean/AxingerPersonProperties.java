package com.github.axinger.model.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "axinger.person")
@Configuration
public class AxingerPersonProperties {

    private Long id;
    private String name;
    private Integer age;

    /// ===================Date===================================
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date createdTime;


    /// ===================LocalDateTime===================================
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedDate;

    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime updatedTime;

    /// ===================String===================================
    private String time;
    /// 12:00:00 会解析成时间戳, 改变格式 "12:00:00"
    private String time2;


    /// list效果一样
//    private List<String> text1;
//    private List<String> text2;
//    private List<String> text3;

    private String text1;
    private String text2;
    private String text3;

    /// 布尔值
    private Boolean enabled;
    private Boolean disabled;

    /// null 值
    private Object empty;
    private Object empty2;
    /// 方式2：方括号
    private List<String> fruits;
    /// 字符串转数字
    private String number;
    /// 显式指定类型
//    private LocalDate date;
    private Date date;

    // LocalDate 类型
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    // LocalDate 列表（Spring Boot 不支持直接绑定 List<LocalDate>，改用 String 列表）
    private List<String> holidays;

    // 嵌套对象
    private Vacation vacation;

    // LocalDateTime 类型
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // LocalTime 类型
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime workStartTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime workEndTime;

    @Data
    public static class Vacation {
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate from;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate to;
    }

}
