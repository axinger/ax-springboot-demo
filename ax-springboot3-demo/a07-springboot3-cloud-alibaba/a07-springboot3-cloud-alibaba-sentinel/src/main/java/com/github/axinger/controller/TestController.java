package com.github.axinger.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2021年12月17日 11:49:00
 */

@RestController
public class TestController {


    @GetMapping("/test1")
    public Object test1() {
        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", "test1");
        System.out.println("map = " + map);
        return map;
    }

    @GetMapping("/test2")
    public Object test12() {
        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", "test2");
        System.out.println("map = " + map);
        return map;
    }

    @GetMapping("/test3")
    public Object test3() {
        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", "test2");
        System.out.println("map = " + map);
        int age = 1 / 0;
        return map;
    }


    /**
     * sentinel 热点限流
     * value 唯一key
     * blockHandler 返回值类型必须与原函数返回值类型一致。
     * 参数例外项
     */
    @GetMapping("/hotKey")
    @SentinelResource(value = "hotKey", blockHandler = "deal_hotKey")
    public Object test4_hotKey(@RequestParam(value = "p1", required = false) String name,
                               @RequestParam(value = "p2", required = false) String name2) {
        System.out.println("name = " + name + ", name2 = " + name2);

        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", name);
        System.out.println("map = " + map);
        return map;
    }

    public String deal_hotKey(String name,
                              String name2,
                              BlockException e) throws Exception {
        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", "热点限流");
        System.out.println("map = " + map + ", e = " + e);

        throw new Exception("热点限流");
    }

    @GetMapping("/hotKey2")
    @SentinelResource(value = "hotKey2", blockHandlerClass = MyHandler.class, blockHandler = "handlerException")
    public Object test4_hotKey2(@RequestParam(value = "p1", required = false) String name,
                                @RequestParam(value = "p2", required = false) String name2) {
        System.out.println("name = " + name + ", name2 = " + name2);

        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", name);
        System.out.println("map = " + map);
        return map;
    }

}
