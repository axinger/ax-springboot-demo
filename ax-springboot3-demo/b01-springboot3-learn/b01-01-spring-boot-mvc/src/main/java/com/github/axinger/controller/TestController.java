package com.github.axinger.controller;

import com.github.axinger.api.OrderApi;
import com.github.axinger.model.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    
    private final OrderApi orderApi;

    @RequestMapping("/1")
    public Object hello(@RequestParam String id) {

        return orderApi.getOrder(id);

    }

    @RequestMapping("/2")
    public Object hello(OrderDTO dto) {

        return orderApi.getOrder2(dto);

    }
}
