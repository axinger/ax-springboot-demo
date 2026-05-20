package com.github.axinger.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * style:
 * <p>
 * NumberFormat.Style.NUMBER: 普通数字格式。
 * NumberFormat.Style.CURRENCY: 货币格式（例如 $1,000.00）。
 * NumberFormat.Style.PERCENT: 百分比格式（例如 50%）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateVO {

    @JsonFormat(pattern = "yyyy-MM-dd") //可以
    private Date date;

    @JsonFormat(pattern = "yyyy-MM") //可以
    private Date date1;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime1;

    /// JsonFormat 可以修改post请求时间格式
    @JsonFormat(pattern = "yyyyAMM-dd HH:mm:ss")      // 负责 JSON 接收和返回
    /// DateTimeFormat 不能修改post请求格式
    @DateTimeFormat(pattern = "yyyyBMM-dd HH:mm:ss")   // 负责 GET 请求和表单接收 ,get请求中不要用中文
    private LocalDateTime localDateTime2;

    private LocalDate localDate1;

}
