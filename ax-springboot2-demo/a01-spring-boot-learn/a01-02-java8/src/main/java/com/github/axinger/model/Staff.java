package com.github.axinger.model;

import com.github.axinger.annotation.DateType;
import com.github.axinger.annotation.TableOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    // 姓名
    String name;
    // 生产日期
    Date date;
    // 产量

    @TableOrder(index = 1, dateType = DateType.VARCHAR)
    BigDecimal yield;

    public static void main(String[] args) {
        DateType len = DateType.VARCHAR.len(1);
    }
}
