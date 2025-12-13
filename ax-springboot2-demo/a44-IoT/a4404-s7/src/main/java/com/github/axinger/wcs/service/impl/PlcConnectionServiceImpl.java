package com.github.axinger.wcs.service.impl;

import com.github.axinger.wcs.entity.ZcPlcInfo;
import com.github.axinger.wcs.job.PlcPool;
import com.github.axinger.wcs.service.PlcConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlcConnectionServiceImpl implements PlcConnectionService {


    @Autowired
    private PlcPool plcPool;


    @Override
    public int getPlcConnectionStatus() {
        ZcPlcInfo plcInfo = new ZcPlcInfo();
        return plcInfo.getPlcStatus();
    }

    @Override
    public boolean open() {

//        ZcPlcInfo plcInfo = plcInfoService.getMainPlc();
//
//        if (Optional.ofNullable(plcInfo).isEmpty()) {
//            return false;
//        }
//        PlcConnection plcConnection = plcPool.open(plcInfo);
//        if (Optional.ofNullable(plcConnection).isEmpty()) {
//            return false;
//        }
        // 添加定时任务
        return true;
    }

    @Override
    public boolean close() {
        plcPool.close();

//        ZcPlcInfo plcInfo = plcInfoService.getMainPlc();
//        if (Optional.ofNullable(plcInfo).isEmpty()) {
//            return false;
//        }
        // 删除定时任务
        return true;
    }
}
