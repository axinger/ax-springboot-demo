package com.github.axinger.seata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.seata.domain.Order;
import io.seata.spring.annotation.GlobalTransactional;

/**
 * @author xing
 * @description 针对表【t_order】的数据库操作Service
 * @createDate 2022-04-03 23:28:50
 */
public interface OrderService extends IService<Order> {

    /**
     * name 唯一性就行,不是conf文件的
     */
    @GlobalTransactional(name = "gt_order_create", rollbackFor = Exception.class)
    boolean create(Order order);
}
