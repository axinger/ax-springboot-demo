package com.github.axinger.controller;

import com.github.axinger.vo.AdminVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Tag(name = "AdminControllerApi", description = "管理员相关接口")
@RestController
@RequestMapping("/admin")
public class AdminController {


    @GetMapping
    @Operation(summary = "管理信息", description = "管理信息")
    public Object data() {
        AdminVo adminVo = new AdminVo();
        adminVo.setPortList(Arrays.asList("add", "delete"));
        return adminVo;
//        return Result.ok(adminVo);
    }
}
