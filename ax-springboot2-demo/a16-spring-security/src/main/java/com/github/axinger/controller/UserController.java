package com.github.axinger.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

//    表达式	作用	前缀处理	适用场景
//    hasRole('user')	检查用户是否有 ROLE_user 角色	自动添加 ROLE_ 前缀	简化角色权限校验
//    hasAuthority('ROLE_user')	检查用户是否有指定的权限字符串（这里是 ROLE_user）	不会自动添加任何前缀	灵活支持角色和细粒度权限校验


    //    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/1")
    public Object adminOnly() {
        Map<String, Object> result = new HashMap<>();
        result.put("1", "2");
        return result;
    }


    //    @PreAuthorize("hasAuthority('add')")
    @GetMapping("/2")
    public Object readPermission() {
        Map<String, Object> result = new HashMap<>();
        result.put("1", "2");
        return result;
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or hasAuthority('select')")
    @GetMapping("/3")
    public Object userOrReadPermission() {
        Map<String, Object> result = new HashMap<>();
        result.put("1", "2");
        return result;
    }

    //    @PreAuthorize("hasAuthority('select')")
    @GetMapping("/4")
    public Object userOrReadPermission4() {
        Map<String, Object> result = new HashMap<>();
        result.put("1", "2");
        return result;
    }


}
