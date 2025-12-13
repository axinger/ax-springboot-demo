package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id; // 订单ID
    private String type; // 类型：VIP/NORMAL
    private BigDecimal amount; // 金额
    private Date createTime; // 创建时间

    private String userId;
}
