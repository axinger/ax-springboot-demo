package com.github.axinger.controller;

import cn.hutool.core.io.IoUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class FileController {

    // 下载文件-jar中的文件

    @GetMapping("/download")
    public void shenbao(@RequestParam String fileName,
                        HttpServletResponse response) throws Exception {
        // 对文件名进行UTF-8编码
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        // 设置响应的内容类型
        // 设置响应的内容类型为PDF
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        // 设置响应的头文件，指定文件名
//        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");


        // 从JAR文件中加载要下载的文件
        Resource resource = new ClassPathResource(fileName);
        @Cleanup InputStream fileInputStream = resource.getInputStream();

        // 获取响应的输出流
        @Cleanup OutputStream outputStream = response.getOutputStream();


        // 使用IOUtils.copy将输入流的内容复制到输出流
//        IOUtils.copy(fileInputStream, outputStream);
        IoUtil.copy(fileInputStream, outputStream);

        // 关闭输入流和输出流
//        fileInputStream.close();
//        outputStream.close();

    }

}
