package com.github.axinger.order.controller;

import com.github.axinger.order.service.OrderFeignService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
@Slf4j
public class OrderController {


    @Value("${server.port}")
    private String port;

    @Resource
    private RestTemplate restTemplate;


    @Value("${server-url.nacos-payment-service}")
    private String paymentURL;

    @Value("${server-url.gateway-service}")
    private String gatewayService;
    @Autowired
    private OrderFeignService orderFeignService;

    /**
     * 直接请求测试一下
     *
     * @return
     */
    @GetMapping(value = "/test")
    public Object order1() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("order_name", "订单");
        map.put("order_port", port);
        return map;

    }

    /**
     * 请求nacos
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/nacos/{id}")
    public Object order(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("order_id", id);
        map.put("order_name", "订单");
        map.put("order_port", port);

        String url = paymentURL + "/payment/count/" + id;
        System.out.println("url = " + url);

        Map<String, Object> map1 = restTemplate.getForObject(url, Map.class);
        map.putAll(map1);
        return map;

    }

    /**
     * 请求网关,利用网关进行分发,需要 服务名+path+接口, 一定要区分path
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/gateway/{id}")
    public Object order2(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("order_id", id);
        map.put("order_name", "订单");
        map.put("order_port", port);

        String url = gatewayService + "/payment/count/" + id;
        System.out.println("url = " + url);

        Map<String, Object> map1 = restTemplate.getForObject(url, Map.class);
        map.putAll(map1);
        return map;

    }

    @GetMapping(value = "/gateway/feign/{id}")
    public Object gatewayFeign(@PathVariable("id") Integer id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("order_id", id);
        map.put("order_name", "订单");
        map.put("order_port", port);

        Map<String, Object> map1 = orderFeignService.count(id);
        map.putAll(map1);

        return map;

    }

}
