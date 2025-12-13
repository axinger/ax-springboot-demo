package com.github.axinger.wcs.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.github.axinger.model.dto.PlcDbDataVO;
import com.github.axinger.model.event.PlcEvent;
import com.github.axinger.model.plc.PLCReadDTO;
import com.github.axinger.wcs.api.PlcFoot;
import com.github.axinger.wcs.entity.ZcPlcInfo;
import com.github.axinger.wcs.service.IZcPlcInfoService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PlcJob {

    @Autowired
    private PlcPool plcPool;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private IZcPlcInfoService plcInfoService;

    public void execute() {
        PlcConnection plcConnection = plcPool.getPlcConnection();
//        Map<Integer, PlcFoot> dataBlockInfoMap1 = plcPool.getDataBlockInfoMap();
        log.info("1s定时读取数据={}", plcPool.getPlcConnectionStatus());
        if (Optional.ofNullable(plcConnection).isEmpty()) {
            ZcPlcInfo plcInfo = plcInfoService.getMainPlc();
            plcPool.open(plcInfo);
        }
        withDataBlock(plcPool.getFootList());
    }


    @SneakyThrows
    protected void withDataBlock(List<PlcFoot> footList) {

        if (ObjUtil.isEmpty(footList)) {
            return;
        }
        try {
            List<PlcDbDataVO> list = footList.stream()
                    .map(foot -> foot.readClass(PLCReadDTO.class)
                            .thenApply(val -> {
                                PlcDbDataVO plcDbDataVO = new PlcDbDataVO();
                                BeanUtil.copyProperties(val.getData(), plcDbDataVO);
                                return mainData(plcDbDataVO, foot.getDbNum());
                            })
                            .exceptionally((e) -> {
                                log.error("plc读取数据错误={}", e.getMessage());
                                PlcDbDataVO plcDbDataVO = new PlcDbDataVO();
                                return mainData(plcDbDataVO, foot.getDbNum());
                            }))
                    .map(CompletableFuture::join)
                    .toList();
            log.info("plc读取到的数据 list = {}", list);
            // 30s过期
//            redisTemplate.opsForValue().set(RedisKeysConfig.plcDataKey, JSON.toJSONString(list), 30, TimeUnit.SECONDS);
            applicationEventPublisher.publishEvent(new PlcEvent(list));
        } catch (Exception e) {
            log.error("publishEvent错误={}", e.getMessage());
        }

    }


    public PlcDbDataVO mainData(PlcDbDataVO bo, int dbNum) {
        bo.setDbNo(dbNum);
        bo.setDate(new Date());
        bo.setDeviceName(StrUtil.format("device{}", dbNum));
        bo.setAlias(StrUtil.format("{}号机器人", dbNum));
        return bo;
    }
}
