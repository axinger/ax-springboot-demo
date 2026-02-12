package com.github.axinger.controller;

import com.github.axinger.model.OrderDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/search")
    public OrderDTO getOrder(@RequestParam String id) {

        return new OrderDTO(id, 10, "111");
    }

    @GetMapping("/search2")
    public OrderDTO getOrder2(OrderDTO dto) {

        return new OrderDTO(dto.id(), 10, "2222");
    }
}
