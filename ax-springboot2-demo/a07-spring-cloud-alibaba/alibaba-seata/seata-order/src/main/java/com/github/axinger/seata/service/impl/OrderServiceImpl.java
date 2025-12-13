package com.github.axinger.seata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.seata.domain.Order;
import com.github.axinger.seata.mapper.OrderMapper;
import com.github.axinger.seata.service.OrderService;
import com.github.axinger.seata.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xing
 * @description 针对表【t_order】的数据库操作Service实现
 * @createDate 2022-04-03 23:28:50
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Autowired
    private StorageService storageService;


    @Override
    /**
     * name 唯一性就行,不是conf文件的
     * */
    @GlobalTransactional(name = "gt_order_create", rollbackFor = Exception.class)
    public boolean create(Order order) {

        log.info("==========新建订单");
        baseMapper.create(order);

        log.info("==========调用库存微服务,减库存");
        storageService.decrease(order.getProductId(), order.getCount());


        log.info("==========修改订单状态");
        baseMapper.updateStatus(order.getUserId(), 0);

        log.info("==========下订单结束");

        return true;
    }
}




