package com.ax.payment.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String port;

    @GetMapping(value = "/count/{id}")
    public Map<String, Object> payment(@PathVariable("id") String id) {
        System.out.println("payment_id = " + id);
        Map<String, Object> map = new HashMap<>(16);
        map.put("payment_id", id);
        map.put("payment_name", "支付");
        map.put("payment_port", port);
        return map;
    }

}
