package com.github.axinger.projectb.controller;

import com.github.axinger.api.order.dto.OrderDTO;
import com.github.axinger.api.order.dto.OrderRequest;
import com.github.axinger.api.order.enums.OrderStatusEnum;
import com.github.axinger.api.order.feign.OrderFeignApi;
import com.github.axinger.common.result.Result;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单服务 Controller
 * 实现了 OrderFeignApi 接口，对外提供订单服务
 */
@RestController
public class OrderController implements OrderFeignApi {

    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<OrderDTO> orders = new ArrayList<>();

    public OrderController() {
        // 初始化一些测试数据
        orders.add(OrderDTO.builder()
                .id(idGenerator.getAndIncrement())
                .orderNo("O202401010001")
                .userId(1L)
                .totalAmount(new BigDecimal("199.99"))
                .status(OrderStatusEnum.PAID.getCode())
                .createTime(LocalDateTime.now())
                .build());
        orders.add(OrderDTO.builder()
                .id(idGenerator.getAndIncrement())
                .orderNo("O202401010002")
                .userId(2L)
                .totalAmount(new BigDecimal("599.00"))
                .status(OrderStatusEnum.SHIPPED.getCode())
                .createTime(LocalDateTime.now())
                .build());
    }

    @Override
    public OrderDTO getById(Long id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<OrderDTO> list() {
        return orders;
    }

    @Override
    public List<OrderDTO> search(OrderRequest request) {
        return orders.stream()
                .filter(o -> request.getUserId() == null || o.getUserId().equals(request.getUserId()))
                .filter(o -> request.getOrderNo() == null || o.getOrderNo().contains(request.getOrderNo()))
                .filter(o -> request.getStatus() == null || o.getStatus().equals(request.getStatus()))
                .toList();
    }

    @Override
    public OrderDTO save(OrderDTO order) {
        if (order.getId() == null) {
            order.setId(idGenerator.getAndIncrement());
            order.setCreateTime(LocalDateTime.now());
            orders.add(order);
        } else {
            orders.removeIf(o -> o.getId().equals(order.getId()));
            orders.add(order);
        }
        return order;
    }

    /**
     * 额外暴露一个非 Feign 接口的端点，用于测试
     */
    public Result<String> hello() {
        return Result.ok("Hello from Project-B (Order Service)");
    }
}
