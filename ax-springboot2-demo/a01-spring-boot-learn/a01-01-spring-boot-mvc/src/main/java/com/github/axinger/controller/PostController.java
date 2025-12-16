package com.github.axinger.controller;

import com.github.axinger.model.dto.LoginDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postTest")
public class PostController {

    // 这个请求不行
    @PostMapping("/login1")
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        return List.of(username, password);
    }

    @PostMapping("/login2")
    public Object login2(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestBody @Validated LoginDTO dto) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("dto", dto);
        return map;
    }

    /**
     * 示例：带复杂校验的POST请求
     */
    @PostMapping("/user")
    public Object createUser(@RequestBody @Validated LoginDTO dto) {
        return dto;
    }
}
