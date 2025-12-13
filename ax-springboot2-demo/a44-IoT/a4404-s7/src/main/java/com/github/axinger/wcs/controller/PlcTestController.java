package com.github.axinger.wcs.controller;

import com.github.axinger.model.dto.DeviceVO;
import com.github.axinger.model.dto.PlcDbDataVO;
import com.github.axinger.model.dto.WsMessageVO;
import com.github.axinger.model.keys.WsCmdKeys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "PLC控制器测试")
@RestController
@RequestMapping("/zhongche/test/plc")
@Validated
@Slf4j
public class PlcTestController {
//    @Autowired
//    private RedisUtil redisUtil;


//     @Operation(summary = "测试数据2")
//    @GetMapping("/2")
//    public Object robo2() {
//
//        List<PlcDbDataVO> list = new ArrayList<>();
//
//        {
//
//            PlcDbDataVO dbDataVO = new PlcDbDataVO();
//            dbDataVO.setDbNo(2);
//            dbDataVO.setWeldingMachinePower(true);
//            dbDataVO.setRobotPower(true);
//            list.add(dbDataVO);
//        }
//        redisUtil.set(RedisKeysConfig.plcDataKey, list);
//        List<PlcDbDataVO> list2 = (List<PlcDbDataVO>) redisUtil.get(RedisKeysConfig.plcDataKey);
//        log.info("plcData={}", list2);
//        return Result.ok(list2);
//    }


    @Operation(summary = "ws实时数据")
    @GetMapping("/3")
    public Object robo3() {

        List<PlcDbDataVO> dataList = new ArrayList<>();

        {
            PlcDbDataVO dbDataVO = new PlcDbDataVO();
            dbDataVO.setDbNo(2);
            dbDataVO.setWeldingMachinePower(true);
            dbDataVO.setRobotPower(true);
            dataList.add(dbDataVO);
        }

        DeviceVO deviceVO = new DeviceVO();
        deviceVO.setDevice(dataList);
        WsMessageVO<DeviceVO> wsMessageVO = new WsMessageVO<>();
        wsMessageVO.setCode(200);
        wsMessageVO.setCmd(WsCmdKeys.PLC_DEVICE_REAl);
        wsMessageVO.setData(deviceVO);
        wsMessageVO.setDate(LocalDateTime.now());
//        SendWebSocket.sendAllMessage(wsMessageVO);
        return dataList;
    }
}
