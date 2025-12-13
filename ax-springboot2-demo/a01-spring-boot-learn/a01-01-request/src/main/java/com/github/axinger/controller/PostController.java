package com.github.axinger.controller;

import com.github.axinger.bean.Person;
import com.github.axinger.bean.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postTest")
@Tag(name = "post请求测试", description = "PostController")
public class PostController {


    // 表单格式的可以用 RequestParam,  JSON格式不可以用 RequestParam
    @PostMapping("/fromData")
    @Operation(summary = "from格式", description = "from表单")
    public Object fromData(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        return List.of(username, password);
    }

    @PostMapping("/jsonData")
    @Operation(summary = "json格式")
    public Object jsonData(@RequestBody User user) {
        return user;
    }


    @PostMapping("/gender")
    @Operation(summary = "json格式,含有枚举")
    public Object login(@RequestBody Person person) {
        System.out.println("枚举作为参数 = " + person);
        return Map.of("枚举参数", person);
    }

}
