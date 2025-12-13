package com.github.axinger.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.axing.common.response.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {

    String name = "orders";

    @SaCheckPermission("orders:get")
    @RequestMapping("/get")
    public Result<?> get() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "get");
        return Result.success(map);
    }

    @SaCheckPermission("orders:add")
    @RequestMapping("/add")
    public Result<?> add() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "add");
        return Result.success(map);
    }

    @RequestMapping("/del")
    public Result<?> del() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "del");
        return Result.success(map);
    }

    @SaCheckRole("LEADER")
    @RequestMapping("/update")
    public Result<?> update() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "update");
        return Result.success(map);
    }
}
