package com.github.axinger.seata.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName t_order
 */
@TableName(value = "t_order")
@Data
@Builder
public class Order implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private Long userId;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 库存数量
     */
    private Integer count;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 0创建中,1完结
     */
    private Integer status;
}
