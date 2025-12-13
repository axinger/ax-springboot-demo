package com.github.axinger.wcs.controller;


import com.github.axinger.model.vo.Result;
import com.github.axinger.wcs.service.PlcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Tag(name = "PLC控制器")
@RestController
@RequestMapping("/zhongche/plc")
@Validated
@Slf4j
public class PLCController {


    @Autowired
    private PlcService plcService;
//    @Resource
//    private RedisTemplate<String, Boolean> redisTemplate;


    @Operation(summary = "机器人开机关机")
    @GetMapping("/robotOn")
    public Result<?> robot(Integer db, Boolean on) {
        boolean b = plcService.robotOn(db, on);
        return Result.ok("发送机器人编号成功");
    }

    @Operation(summary = "焊机开机关机")
    @GetMapping("/weldingMachineOn")
    public Result<?> sendDb(Integer db, Boolean on) {
        boolean b = plcService.weldingMachineOn(db, on);
        return Result.ok("发送机器人编号成功");
    }

    @Operation(summary = "机器人复位")
    @GetMapping("/reset")
    public Result<?> reset(Integer db) {
        boolean b = plcService.reset(db);
        return Result.ok("发送机器人复位命令成功");
    }

    @Operation(summary = "机器人再启动")
    @GetMapping("/restart")
    public Result<?> restart(Integer db) {
        boolean b = plcService.restart(db);
        return Result.ok("发送机器人再启动命令成功");
    }

    @Operation(summary = "发送PLC数据-焊缝")
    @GetMapping("/sendLine")
    public Result<?> sendLine(@NotNull(message = "db编号不能为空") Integer dbNo,
                              @NotNull(message = "焊缝不能为空") Integer line) {
        boolean b = plcService.sendLine(dbNo, line);
        return Result.ok("发送焊缝成功");
    }

    @Operation(summary = "发送PLC数据-参数确认")
    @GetMapping("/sendParameter")
    public Result<?> sendParameter(@NotNull(message = "db编号不能为空") Integer dbNo,
                                   @NotNull(message = "焊缝不能为空") Integer line) {
        plcService.sendParameter(dbNo, line);
        return Result.OK("发送参数确认成功");
    }

    @Operation(summary = "发送PLC数据-开始任务")
    @GetMapping("/sendStart")
    public Result<?> sendStart(@NotNull(message = "db编号不能为空") Integer dbNo,
                               @NotNull(message = "焊缝不能为空") Integer line) {
        plcService.sendStart(dbNo, line);
        return Result.OK("发送开始任务成功");
    }

    @Operation(summary = "发送PLC数据-x油泵")
    @GetMapping("/sendOilPumpX")
    public Result<?> sendOilPumpX(@NotNull(message = "db编号不能为空") Integer dbNo) {
        boolean res = plcService.sendOilPump(dbNo);
        return Result.OK(res);
    }

    @Operation(summary = "龙门架进仓")
    @GetMapping("/allInput")
    public Result<?> allInput() {

//        Boolean isInput = redisTemplate.opsForValue().get(RedisKeysConfig.plcInputStatusKey);
//        if (Boolean.TRUE.equals(isInput)) {
//            throw new RuntimeException("龙门架正在出仓中");
//        }
//        plcService.allInput();
        return Result.OK("发送门架进仓指令成功");
    }

    @Operation(summary = "龙门架出仓")
    @GetMapping("/allOutput")
    public Result<?> allOutput() {
//        Boolean isInput = redisTemplate.opsForValue().get(RedisKeysConfig.plcOutputStatusKey);
//        if (Boolean.TRUE.equals(isInput)) {
//            throw new RuntimeException("龙门架正在出仓中");
//        }
//        plcService.allOutput();
        return Result.OK("发送龙门架出仓指令成功");
    }

}
