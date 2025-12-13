package com.github.axinger.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RequestMappingController {


    @RequestMapping("/request")
    public Object RequestMapping(String id) {
        return List.of(id);
    }

    @GetMapping("/get")
    public Object GetMapping(String id) {
        return List.of(id);
    }


    // 走的是get方式拼接参数,不要误会走body
    // http://localhost:8080/post/data?name=d&age=111
    @PostMapping("/post/data")
    public Object PostMappingData(String name, Integer age) {
        Map<String, Object> map = Map.of("name", name, "age", age);
        System.out.println("map = " + map);
        return map;
    }

    @PutMapping("/put")
    public Object PutMapping(String id) {
        return List.of(id);
    }


    @DeleteMapping("/delete")
    public Object DeleteMapping(String id) {
        return List.of(id);
    }

    @PatchMapping("/patch")
    public Object PatchMapping(String id) {
        return List.of(id);
    }
}
