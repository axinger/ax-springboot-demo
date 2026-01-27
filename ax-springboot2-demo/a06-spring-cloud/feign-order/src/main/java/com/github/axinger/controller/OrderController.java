package com.github.axinger.controller;

import cn.hutool.http.HttpUtil;
import com.github.axinger.api.PaymentApi;
import com.github.axinger.api.PaymentApi2;
import com.github.axinger.api.PaymentApi4;
import com.github.axinger.dto.PaymentApiDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private PaymentApi paymentApi;
    @Autowired
    private PaymentApi2 paymentApi2;
    @Autowired
    private PaymentApi4 paymentApi4;

    @Operation(summary = "直接调用请求支付系统")
    @GetMapping(value = "/test")
    public Object order1() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", "1");

        Map<String, Object> abc123 = paymentApi.test1("abc123", map);

        return abc123;
    }

    @GetMapping(value = "/test2")
    public Object test2() {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", "1");

        Map<String, Object> abc123 = paymentApi2.test1(map);

        return abc123;
    }


    @GetMapping(value = "/test4")
    public Object test4(String orderId) {

        PaymentApiDTO.PaymentDTO paymentDTO = new PaymentApiDTO.PaymentDTO();
        paymentDTO.setOrderId(orderId);

        PaymentApiDTO.PaymentVO vo = paymentApi4.payment(paymentDTO);

        return vo;
    }


    @GetMapping("/test5")
    public Object test5(HttpServletRequest request) {
        String queryString = request.getQueryString(); // 如 "orderId=123+456"

//        String urlWithForm = HttpUtil.urlWithForm("http://localhost:8080/order/test4", queryString);
//        Map<String, List<String>> map = HttpUtil.decodeParams(queryString, StandardCharsets.UTF_8);
        Map<String, String> map = HttpUtil.decodeParamMap(queryString, StandardCharsets.UTF_8);


        // 手动解析，且不做 + → 空格 的转换
        // 注意：这需要自己实现解析逻辑，或使用 URLEncodedUtils 并禁用空格转换（较复杂）

        return map;
    }

    @GetMapping("/test6")
    public Object test6(HttpServletRequest request) {


        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("orderId", "123+456");


        String url2 = HttpUtil.urlWithFormUrlEncoded("http://localhost:10707/order/test4",paramMap, StandardCharsets.UTF_8);
        System.out.println("url2 = " + url2);
        String url3 = HttpUtil.urlWithForm("http://localhost:10707/order/test4", paramMap, StandardCharsets.UTF_8, true);
        System.out.println("url3 = " + url3);

        String result3 = HttpUtil.get("http://localhost:10707/order/test4", paramMap);
        return result3;
    }
}
