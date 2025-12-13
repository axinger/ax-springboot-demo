package com.github.axinger.wcs.service.impl;

import com.github.axinger.wcs.entity.ZcPlcInfo;
import com.github.axinger.wcs.service.IZcPlcInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: plc信息
 * @Author: jeecg-boot
 * @Date: 2024-04-18
 * @Version: V1.0
 */
@Service
public class ZcPlcInfoServiceImpl implements IZcPlcInfoService {

    @Override
    public ZcPlcInfo getMainPlc() {
        return null;
    }

    @Override
    public List<ZcPlcInfo> getSubList() {
        return null;
    }
}
