package com.github.axinger.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    // 下载文件-jar中的文件

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/0")
    public void test0(@RequestParam String fileName,
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

    /// jar无法获取
    /// 只支持文件系统路径（file:）
    /// 但一旦项目被打包成 JAR 文件，资源文件就 嵌入到了 JAR 包内部，它们不再是文件系统中的独立文件，而是以流的形式存在于归档包中，路径类似
    /// jar:file:/app.jar!/BOOT-INF/classes!/data.txt
    /// 因为 jar:file: 不是 file: 协议，无法转换为 java.io.File 对象
    //org.springframework.util.ResourceUtils
    @GetMapping("/1")
    public Object test1() throws Exception {
        System.out.println("java当前目录,main函数,和springboot 项目不一样: " + System.getProperty("user.dir"));
        File file = org.springframework.util.ResourceUtils.getFile("classpath:123.json");
        String path = file.getAbsolutePath();
        String data = FileUtil.readString(file, StandardCharsets.UTF_8);
        return Map.of("path", path, "data", data);
    }

    //org.springframework.core.io.ClassPathResource
    @GetMapping("/2")
    public Object test2() throws Exception {
        ClassPathResource resource = new ClassPathResource("123.json");
        String path = resource.getPath();
        @Cleanup InputStream inputStream = resource.getInputStream();
        String data = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        return Map.of("path", path, "data", data);
    }

    /// 能获取
    @GetMapping("/21")
    public Object test21() throws Exception {
        ClassPathResource resource = new ClassPathResource("123.json");
        @Cleanup InputStream inputStream = resource.getInputStream();
        String data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return Map.of("path", resource.getPath(), "data", data);
    }

    /// 能获取
    @GetMapping("/3")
    @SneakyThrows
    public Object test3() {
        @Cleanup InputStream inputStream = getClass().getClassLoader().getResourceAsStream("123.json");
        String data = Optional.ofNullable(inputStream).map(val -> {
            try {
                return new String(val.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                return "数据异常";
            }
        }).orElse("数据异常");
        return Map.of("path", "classpath:123.json", "data", data);
    }

    /// 能获取
    @GetMapping("/31")
    @SneakyThrows
    public Object test31() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:123.json");
        @Cleanup InputStream inputStream = resource.getInputStream();
        String data = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        return Map.of("path", "classpath:123.json", "data", data);
    }

    /// 能获取
    //cn.hutool.core.io.resource
    @GetMapping("/4")
    public Object test4() {
        String data = ResourceUtil.readUtf8Str("123.json");

        //当资源不存在时返回null
        InputStream inputStream = ResourceUtil.getStreamSafe("123.json");
        System.out.println("inputStream = " + inputStream);

        URL url = ResourceUtil.getResource("123.json");
        System.out.println("url = " + url);

        List<URL> urlList = ResourceUtil.getResources("dir1/");
        System.out.println("urlList = " + urlList);

        cn.hutool.core.io.resource.Resource resource = ResourceUtil.getResourceObj("123.json");
        URL url1 = resource.getUrl();


        String data2 = resource.readUtf8Str();
        return Map.of("url", url, "url1", url1, "data", data, "data2", data2);
    }
}
