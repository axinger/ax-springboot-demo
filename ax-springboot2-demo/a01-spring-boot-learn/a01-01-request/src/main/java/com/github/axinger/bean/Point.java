package com.github.axinger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Point {

    @JsonProperty(value = "xPoint")
    private String xPoint;

    @JsonProperty(value = "yPoint")
    private String yPoint;

    @JsonProperty(value = "zPoint")
    private String zPoint;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
