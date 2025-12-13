package com.github.axinger.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 品牌管理Controller
 * Created by macro on 2019/4/19.
 */
//@Tag(name = "PmsBrandController", description = "Tag注解")
@Tag(name = "控制器Tag-name", description = "Tag-description")
@RestController
@RequestMapping("/brand")
public class PmsBrandController {


    @Operation(summary = "方法 Operation注解 ", description = "description注解")
    @GetMapping(value = "/list/{name}")
    public Object getBrandList(@Parameter(name = "用户名") @PathVariable String name) {
        return true;
    }


}
