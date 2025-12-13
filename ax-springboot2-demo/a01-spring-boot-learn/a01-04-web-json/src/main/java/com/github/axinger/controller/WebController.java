package com.github.axinger.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController {
    /**
     * fromData
     *
     * @param name
     * @param age
     * @return
     */
    @PostMapping(value = "/web1")
    public Object fromData(@RequestParam("name") String name, @RequestParam Integer age) {
        Map<String, Object> map = new HashMap<>();
        map.put("localDateTime", LocalDateTime.now());
        map.put("name", name);
        map.put("age", age);
        return map;

    }

    /**
     * @param json
     * @return
     * @RequestBody只能有一个
     */
    @PostMapping(value = "/web2")
    public Object json(@RequestBody JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        map.put("localDateTime", LocalDateTime.now());
        map.put("name", json.getString("name"));
        map.put("age", json.getIntValue("age"));
        return map;

    }
}
