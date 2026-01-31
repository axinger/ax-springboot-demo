package com.github.axinger.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 */
@Slf4j
@RestController
public class PaymentController {

    @Value("${server.port}")
    private String port;


    @GetMapping(value = "/payment/test1")
    public Map<String, Object> test1(@RequestParam(value = "id") String id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        map.put("name", "test1");
        map.put("port", port);
        map.put("date", LocalDateTimeUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss"));
        log.info("test1 ,id=={}", id);
        return map;
    }


    @GetMapping(value = "/payment/test2")
    public Map<String, Object> test2(@RequestParam(value = "id") String id) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        map.put("name", "test2");
        map.put("port", port);
        map.put("date", LocalDateTimeUtil.format(LocalDateTime.now(),"yyyy-MM-dd HH:mm:ss"));
        log.info("test2 ,id=={}", id);
        return map;
    }

}
