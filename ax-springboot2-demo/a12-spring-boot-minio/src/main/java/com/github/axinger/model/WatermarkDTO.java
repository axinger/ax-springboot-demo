package com.github.axinger.model;


import lombok.Data;

@Data
public class WatermarkDTO {
    private String text;
    private Integer size = 20;
    private Integer opacity = 50;
    private String gravity = "south_east";
}
