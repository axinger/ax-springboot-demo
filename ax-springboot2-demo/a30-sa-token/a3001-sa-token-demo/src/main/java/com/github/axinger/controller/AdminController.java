package com.github.axinger.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.axing.common.response.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    String name = "admin";

    // 角色校验：必须具有指定角色才能进入该方法 
    @SaCheckRole("admin")
    @RequestMapping("/get")
    public Result<?> get() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "get");
        return Result.success(map);
    }

    // 权限校验：必须具有指定权限才能进入该方法 
    @SaCheckPermission("admin:add")
    @RequestMapping("/add")
    public Result<?> add() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "add");
        return Result.success(map);
    }

    // 注解式鉴权：只要具有其中一个权限即可通过校验 
    @SaCheckPermission(value = {"user-add", "user-all", "user-delete"}, mode = SaMode.OR)
    @RequestMapping("/del")
    public Result<?> del() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "del");
        return Result.success(map);
    }

    @RequestMapping("/update")
    public Result<?> update() {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("path", "update");
        return Result.success(map);
    }
}
