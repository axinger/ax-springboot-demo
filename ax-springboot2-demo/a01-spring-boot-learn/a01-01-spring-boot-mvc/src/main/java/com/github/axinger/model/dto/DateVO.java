package com.github.axinger.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


//    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "#,###.##")
//    private double total;
//
//    @NumberFormat(style = NumberFormat.Style.PERCENT)
//    private double percent;
//
//    @NumberFormat(style = NumberFormat.Style.CURRENCY)
//    private double money;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd") //可以
    private Date date;

    @JsonFormat(pattern = "yyyy-MM") //可以
    private Date date1;

    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//可以,不能yyyy-MM-dd
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date2;

}
