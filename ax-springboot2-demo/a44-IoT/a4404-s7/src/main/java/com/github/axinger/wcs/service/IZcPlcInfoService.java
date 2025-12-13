package com.github.axinger.wcs.service;

import com.github.axinger.wcs.entity.ZcPlcInfo;

import java.util.List;

public interface IZcPlcInfoService {

    ZcPlcInfo getMainPlc();

    List<ZcPlcInfo> getSubList();
}
