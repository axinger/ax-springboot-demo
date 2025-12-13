package com.github.axinger.service;

import com.github.axinger.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// 批处理服务
@Slf4j
@Service
public class BatchOrderService {
    public void processBatch(List<Order> orders) {
        log.info("批处理订单：{}条，用户ID：{}", orders.size(), orders);
        // 批量保存数据库、批量调用外部API等
    }
}
