package com.ax.seata.mapper;

import com.ax.seata.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author xing
 * @description 针对表【t_order】的数据库操作Mapper
 * @createDate 2022-04-03 23:28:50
 * @Entity com.ax.seata.domain.Order
 */
public interface OrderMapper extends BaseMapper<Order> {

    // 创建订单
    void create(Order order);

    boolean updateStatus(Long userId, Integer status);
}




