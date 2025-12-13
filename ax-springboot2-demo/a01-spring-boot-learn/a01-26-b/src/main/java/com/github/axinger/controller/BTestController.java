package com.github.axinger.controller;

import com.github.axinger.util.BUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BTestController {

    @GetMapping("/b")
    public Object index() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", BUtil.code());
        return map;
    }
}
