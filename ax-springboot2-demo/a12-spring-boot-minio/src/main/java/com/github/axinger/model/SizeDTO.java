package com.github.axinger.model;


import lombok.Data;

@Data
public class SizeDTO {
    private String bucketName;
    private String path;
    private Integer width;
    private Integer height;
    private Integer quality;
    private WatermarkDTO watermark;
}
