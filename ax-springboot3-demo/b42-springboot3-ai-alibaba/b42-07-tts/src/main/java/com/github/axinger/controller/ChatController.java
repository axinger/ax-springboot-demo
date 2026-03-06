package com.github.axinger.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAudioSpeechApi;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class ChatController {


    private final TextToSpeechModel textToSpeechModel;

    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "支付宝到账10元") String msg) {
        DashScopeAudioSpeechOptions speechOptions = DashScopeAudioSpeechOptions.builder()
                .model(DashScopeModel.AudioModel.COSYVOICE_V1.getValue())
                .voice("longhua")
                .responseFormat(DashScopeAudioSpeechApi.ResponseFormat.MP3)
                .speed(1.0)
                .sampleRate(48000)
                .volume(50)
                .pitch(1.0)
                .build();

        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt(msg, speechOptions);
        TextToSpeechResponse response = textToSpeechModel.call(speechPrompt);

        byte[] output = response.getResult().getOutput();

        String fileName = "d:\\" + IdUtil.fastSimpleUUID() + ".mp3";
//        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
//            fileOutputStream.write(output);
//        } catch (Exception e) {
//            System.out.println("e = " + e);
//        }

        File file = FileUtil.writeBytes(output, fileName);

        String absolutePath = file.getAbsolutePath();
        System.out.println("absolutePath = " + absolutePath);

        return fileName;
    }

}
