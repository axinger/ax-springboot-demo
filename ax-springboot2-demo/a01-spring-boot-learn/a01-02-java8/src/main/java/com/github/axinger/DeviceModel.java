package com.github.axinger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceModel {
    private String id;
    private String state;
}
