package com.github.axinger.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "tags值", description = "value不显示")
@RestController("")
public class HoneController {

    @ApiOperation("参数接口")
    @GetMapping(value = "/group1/login")
    public Object login(@ApiParam(value = "姓名") String name) {

        Map map = new HashMap<String, Object>(3);
        map.put("success", true);
        return map;
    }

    @ApiOperation("参数接口")
    @GetMapping(value = "/group2/login")
    public Object group2(@ApiParam(value = "姓名") String name) {

        Map map = new HashMap<String, Object>(3);
        map.put("success", true);
        return map;
    }
}
