package com.github.axinger.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName MyHandler.java
 * @Description TODO
 * @createTime 2021年12月17日 16:34:00
 */

public class MyHandler {

    public static Object handlerException(BlockException e) throws Exception {
        Map<Object, Object> map = new HashMap<>(16);
        map.put("val", "热点限流");
        System.out.println("map = " + map + ", e = " + e);
        throw new Exception("热点限流");
    }
}
