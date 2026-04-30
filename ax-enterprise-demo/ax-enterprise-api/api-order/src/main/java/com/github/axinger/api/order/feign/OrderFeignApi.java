package com.github.axinger.api.order.feign;

import com.github.axinger.api.order.dto.OrderDTO;
import com.github.axinger.api.order.dto.OrderRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单服务 Feign 接口定义
 * 服务提供方实现此接口，消费方通过 Feign 调用
 */
@RequestMapping("/api/order")
public interface OrderFeignApi {

    @GetMapping("/{id}")
    OrderDTO getById(@PathVariable("id") Long id);

    @GetMapping("/list")
    List<OrderDTO> list();

    @PostMapping("/search")
    List<OrderDTO> search(@RequestBody OrderRequest request);

    @PostMapping
    OrderDTO save(@RequestBody OrderDTO order);
}
