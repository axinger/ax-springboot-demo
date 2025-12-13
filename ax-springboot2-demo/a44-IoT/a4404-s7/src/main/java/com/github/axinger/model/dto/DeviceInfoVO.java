package com.github.axinger.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeviceInfoVO {
    @JsonProperty("taskNo")
    private String taskNo;
    @JsonProperty("user")
    private String user;

    @JsonProperty("pressure")
    private float pressure;
    @JsonProperty("current")
    private float current;
}
