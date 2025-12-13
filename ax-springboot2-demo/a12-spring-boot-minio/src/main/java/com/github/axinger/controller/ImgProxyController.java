package com.github.axinger.controller;

import com.axing.common.response.dto.Result;
import com.github.axinger.model.SizeDTO;
import com.github.axinger.service.ImgProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("img")
public class ImgProxyController {

    @Autowired
    private ImgProxyService imgProxyService;

    /**
     * 获取指定尺寸图片
     *
     * @return /  watermark=text:版权@axing;size:16;opacity:70;gravity:north_west
     */
    @PostMapping(value = "size1")
    public Result size1(@RequestBody SizeDTO dto) {

        String bucketName = dto.getBucketName();
        String path = dto.getPath();
        Integer width = dto.getWidth();
        Integer height = dto.getHeight();
        String processingOptions = "s:" + width + ":" + height + ":1:1";
//        String processingOptions = "s:" + width  + ":1:1";
        String imgProxyUrl = imgProxyService.getImgProxyUrl(bucketName, path, processingOptions);
        return Result.success(Map.of("url", imgProxyUrl));
    }

    @PostMapping(value = "size2")
    public Result size2(@RequestBody SizeDTO dto) {

        String bucketName = dto.getBucketName();
        String path = dto.getPath();
        Integer width = dto.getWidth();
        Integer height = dto.getHeight();
        Integer quality = dto.getQuality();

        StringBuilder processingOptions = new StringBuilder("resize:");

        // ===== 处理缩放 =====
        if (width != null && height != null) {
            processingOptions.append("fit:").append(width).append(":").append(height);
        } else if (width != null) {
            processingOptions.append("width:").append(width);
        } else if (height != null) {
            processingOptions.append("height:").append(height);
        } else {
            processingOptions.append("fit:800:600"); // 默认
        }

        if (quality != null) {
            processingOptions.append("/q:").append(quality); // 质量
        }

        // ===== 处理水印 =====

        if (dto.getWatermark() != null) {
            String wm = imgProxyService.buildWatermarkParam(dto.getWatermark());
            if (wm != null) {
                processingOptions.append("/").append(wm);
            }
        }

        String imgProxyUrl = imgProxyService.getImgProxyUrl(bucketName, path, processingOptions.toString());
        return Result.success(Map.of("url", imgProxyUrl));
    }


    /**
     * 获取压缩后的图片
     *
     * @return /
     */
    @GetMapping(value = "compOriginal")
    public Result compressionOriginal(@RequestParam("bucketName") String bucketName,
                                      @RequestParam("path") String path) {
        String imgProxyUrl = imgProxyService.getImgProxyUrl(bucketName, path, null);

        return Result.success(Map.of("url", imgProxyUrl));
    }
}
