package com.github.axinger;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @Autowired
    private OcrProperties ocrProperties;

    @PostMapping("/extract")
    public String extractText(@RequestParam("file") MultipartFile file) {
        try {
            // 保存上传的文件
            File tempFile = new File(ocrProperties.getUploadDir() + file.getOriginalFilename());
            FileUtils.writeByteArrayToFile(tempFile, file.getBytes());

            // 调用 Python 脚本
            ProcessBuilder processBuilder = new ProcessBuilder(ocrProperties.getPythonPath(), ocrProperties.getScriptPath(), tempFile.getAbsolutePath());
            Process process = processBuilder.start();
            process.waitFor();

            // 读取输出
            String output = new String(process.getInputStream().readAllBytes());
            return output;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "OCR 识别失败";
        }
    }
}
