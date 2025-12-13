package com.github.axinger.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DeviceVO {

    @JsonProperty("device")
    private List<PlcDbDataVO> device;

    @JsonProperty("deviceInfo")
    private DeviceInfoVO deviceInfo;
}
