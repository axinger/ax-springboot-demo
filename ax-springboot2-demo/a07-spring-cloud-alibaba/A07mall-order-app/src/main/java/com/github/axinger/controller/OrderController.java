package com.github.axinger.controller;

import brave.Tracer;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@Slf4j
@RestController
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
    private Tracer tracer;

    /**
     * 直接请求测试一下
     *
     * @return
     */
    @Operation(summary = "订单系统")
    @GetMapping(value = "/list")
    public Object order1() {
        Map<String, Object> res = new HashMap<>(16);
        res.put("data", "订单系统");
        return res;
    }

    /**
     * 请求nacos,<a href="http://mall-payment-app">...</a>
     *
     * @param id
     * @return
     */
    @Operation(summary = "restTemplate方式,请求支付系统", description = "gatewayFeign")
    @GetMapping(value = "/nacos/{id}")
    public Object order(@PathVariable("id") Integer id) {
        String url = paymentURL + "/payment/count/" + id;
        System.out.println("url = " + url);

        ///  泛型擦除
        Map map2 = restTemplate.getForObject(url, Map.class);
        Map<String, Object> res = new HashMap<>(16);
        res.put("payment", map2);
        return res;
    }

    /**
     * 请求网关,利用网关进行分发,需要 服务名+path+接口, 一定要区分path
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/gateway/{id}")
    public Object order2(@PathVariable("id") Integer id) {

        String url = gatewayService + "/payment/count/" + id;
        System.out.println("url = " + url);
        // 处理嵌套泛型
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("token", "jim");

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<>() {
        });
        Map<String, Object> map2 = response.getBody();
        Map<String, Object> res = new HashMap<>(16);
        res.put("payment", map2);
        return res;

    }


}
