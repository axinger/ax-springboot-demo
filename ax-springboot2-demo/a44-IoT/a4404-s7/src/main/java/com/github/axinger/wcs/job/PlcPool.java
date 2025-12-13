package com.github.axinger.wcs.job;

import com.github.axinger.model.plc.PLCReadDTO;
import com.github.axinger.wcs.api.PlcFoot;
import com.github.axinger.wcs.entity.ZcPlcInfo;
import com.github.axinger.wcs.service.IZcPlcInfoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.PlcConnectionManager;
import org.apache.plc4x.java.api.PlcDriverManager;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@Getter
public class PlcPool {

    public Map<Integer, PlcFoot> dataBlockInfoMap = new HashMap<>();
    public PlcConnection plcConnection = null;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    private Boolean plcConnectionStatus = null;

    private List<PlcFoot> footList = new ArrayList<>();


    @Autowired
    private IZcPlcInfoService plcInfoService;

//    public int getPlcConnectionStatus() {
//        if (null == plcConnectionStatus) {
//            return 0;
//        }
//        if (!plcConnectionStatus) {
//            return 1;
//        }
//        return 2;
//    }


    public void close() {
        plcConnection = null;
        plcConnectionStatus = null;
        footList = new ArrayList<>();
        dataBlockInfoMap = new HashMap<>();
    }


    public PlcConnection open(ZcPlcInfo mainPlcInfo) {

        if (Optional.ofNullable(mainPlcInfo).map(ZcPlcInfo::getIp).isEmpty()) {
            log.error("plc IP 不存在");
            return null;
        }
        PlcConnectionManager plcConnectionManager = PlcDriverManager.getDefault().getConnectionManager();

        if (null == plcConnectionManager) {
            return null;
        }
        String url = "s7://" + mainPlcInfo.getIp();
        try {
            plcConnection = plcConnectionManager.getConnection(url);
            if (!plcConnection.getMetadata().isReadSupported()) {
                log.error("PLC不支持读取数据");
                plcConnectionStatus = false;
                return null;
            }
        } catch (Exception e) {
            log.error("PLC链接ip:{},失败:{}", url, e.getMessage());
            plcConnectionStatus = false;
            return null;
        }
        if (null == plcConnection) {
            log.error("PLC没有链接上{}............", url);
            plcConnectionStatus = false;
            return null;
        }
        plcConnectionStatus = true;
        List<PlcFoot> footList = new ArrayList<>();

        for (ZcPlcInfo plcInfo : plcInfoService.getSubList()) {
            PlcReadRequest.Builder read = plcConnection.readRequestBuilder();
            Integer dbNo = plcInfo.getDbNo();
            PlcFoot foot = new PlcFoot(dbNo, plcConnection, read);
            foot.register(PLCReadDTO.class);
            dataBlockInfoMap.put(dbNo, foot);
            footList.add(foot);
        }
        this.footList = footList;

        log.info("open={}", plcConnection);
        return plcConnection;
    }
//
//    @SneakyThrows
//    protected void withDataBlock(List<PlcFoot> footList) {
//
//        if (ObjUtil.isEmpty(footList)) {
//            return;
//        }
//        try {
//            List<PlcDbDataVO> list = footList.stream()
//                    .map(foot -> foot.readClass(PLCReadDTO.class)
//                            .thenApply(val -> {
////                                    log.info("plc读取到的数据 = {}", val);
//                                PlcDbDataVO plcDbDataVO = new PlcDbDataVO();
//                                BeanUtil.copyProperties(val.getData(), plcDbDataVO);
//                                return mainData(plcDbDataVO, foot.getDbNum());
//                            })
//                            .exceptionally((e) -> {
//                                log.error("plc读取数据错误={}", e.getMessage());
//                                PlcDbDataVO plcDbDataVO = new PlcDbDataVO();
//                                return mainData(plcDbDataVO, foot.getDbNum());
//                            }))
//                    .map(CompletableFuture::join)
//                    .toList();
////                log.info("plc读取到的数据 list = {}", list);
//
//            // 30s过期
//            redisTemplate.opsForValue().set("plc:data", list, 30, TimeUnit.SECONDS);
//            applicationEventPublisher.publishEvent(new PlcEvent(list));
//        } catch (Exception e) {
//            log.error("publishEvent错误={}", e.getMessage());
//        }
//
//    }
//
//    public PlcDbDataVO mainData(PlcDbDataVO bo, int dbNum) {
//        bo.setDbNo(dbNum);
//        bo.setDate(new Date());
//        bo.setDeviceName(StrUtil.format("device{}", dbNum));
//        bo.setAlias(StrUtil.format("{}号机器人", dbNum));
//        return bo;
//    }
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) {
////        log.info("1s定时读取数据={}", plcConnection);
//        Map<Integer, PlcFoot> dataBlockInfoMap1 = PlcPool.staticDataBlockInfoMap;
//        PlcConnection plcConnection1 = PlcPool.plcConnection;
//        System.out.println("plcConnection1 = " + plcConnection1);
//        log.info("1s定时读取数据={}", dataBlockInfoMap1);
//
//
//        if (Optional.ofNullable(plcConnection).isEmpty()) {
//            open();
//        }
//        withDataBlock(this.footList);
//    }
}
