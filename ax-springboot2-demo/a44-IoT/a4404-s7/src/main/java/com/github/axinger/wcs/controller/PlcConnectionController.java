package com.github.axinger.wcs.controller;

import com.github.axinger.model.vo.Result;
import com.github.axinger.wcs.service.PlcConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PLC定时任务")
@RestController
@RequestMapping("/zhongche/plc/connection")
@Validated
public class PlcConnectionController {


    @Autowired
    private PlcConnectionService plcConnectionService;

    @Operation(summary = "结束连接")
    @GetMapping("/status")
    public Result<?> status() {
        return Result.ok(plcConnectionService.getPlcConnectionStatus());
    }


    @Operation(summary = "开始连接")
    @GetMapping("/open")
    public Result<?> open() {
        boolean open = plcConnectionService.open();
        return open ? Result.ok("连接总控成功") : Result.error("连接总控失败");
    }

    @Operation(summary = "结束连接")
    @GetMapping("/close")
    public Result<?> close() {
        boolean close = plcConnectionService.close();
        return close ? Result.ok("停止连接总控成功") : Result.error("停止连接总控失败");
    }

}
