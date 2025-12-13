package com.github.axinger.seata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.seata.domain.Order;

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




