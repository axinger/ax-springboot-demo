package com.github.axinger.api;

import com.github.axinger.model.OrderDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange(url = "http://localhost:20101/order")
public interface OrderApi {

    @GetExchange("/search")
    OrderDTO getOrder(@RequestParam String id);

    @GetExchange("/search2")
    OrderDTO getOrder2(OrderDTO dto);
}
