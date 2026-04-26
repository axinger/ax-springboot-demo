package com.github.axinger.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 产品数据传输对象
 * 用于测试 Groovy 脚本中的对象操作
 */
@Data
public class ProductDTO {

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品单价
     */
    private BigDecimal productPrice;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 计算后的总价（Groovy 脚本会计算此值）
     */
    private BigDecimal total;

    /**
     * 描述信息（Groovy 脚本会设置此值）
     */
    private String desc;

    /**
     * 测试用的 double 类型字段 a
     */
    private BigDecimal a;

    /**
     * 测试用的 double 类型字段 b
     */
    private BigDecimal b;
}
