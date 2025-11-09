package com.github.axinger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TestDTO {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("device1")
        private Device1DTO device1;

        @JsonProperty("device2")
        private Device1DTO device2;

        @JsonProperty("device3")
        private Device1DTO device3;

        @JsonProperty("device4")
        private Device1DTO device4;

        @JsonProperty("deviceInfo")
        private DeviceInfoDTO deviceInfo;

        @NoArgsConstructor
        @Data
        public static class Device1DTO {
            @JsonProperty("on")
            private Boolean on;
            @JsonProperty("location")
            private float location;
            @JsonProperty("x")
            private float x;
            @JsonProperty("y")
            private float y;
            @JsonProperty("pressure")
            private float pressure;
            @JsonProperty("current")
            private float current;
        }

        @NoArgsConstructor
        @Data
        public static class DeviceInfoDTO {
            @JsonProperty("taskNo")
            private String taskNo;
            @JsonProperty("user")
            private String user;
        }
    }
}
