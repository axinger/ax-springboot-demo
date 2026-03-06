package com.github.axinger.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeImageApi;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageGeneration;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ImageModel imageModel;

    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "大白兔") String msg) {
        var dashScopeImageApi = DashScopeImageApi.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .build();

        var dashScopeImageModel = new DashScopeImageModel(dashScopeImageApi);

        var imageOptions = DashScopeImageOptions.builder()
                .model(DashScopeModel.ImageModel.QWEN_IMAGE.getValue())
                .n(1)
                .width(1024)
                .height(1024)
                .style("photography")
                .build();

        var imagePrompt = new ImagePrompt(msg, imageOptions);
        ImageResponse response = dashScopeImageModel.call(imagePrompt);

        List<ImageGeneration> images = response.getResults();

        return response
                .getResult().getOutput().getUrl();
    }


}
