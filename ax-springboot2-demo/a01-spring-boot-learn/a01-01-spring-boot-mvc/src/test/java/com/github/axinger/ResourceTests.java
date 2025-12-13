package com.github.axinger;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
public class ResourceTests {

    //org.springframework.util.ResourceUtils
    @Test
    public void test1() throws Exception {
        System.out.println("java当前目录,main函数,和springboot 项目不一样: " + System.getProperty("user.dir"));

        /// 不能用于生产
        File file = ResourceUtils.getFile("classpath:123.json");
        String path1 = file.getAbsolutePath();

        System.out.println("path1 = " + path1);

        String read = FileUtil.readString(file, StandardCharsets.UTF_8);
        System.out.println("read = " + read);
        // 读取服务器文件
        File file1 = ResourceUtils.getFile("file:/opt/234.json");
        String read2 = FileUtil.readString(file1, StandardCharsets.UTF_8);
        System.out.println("read2 = " + read2);
//        File file1 = ResourceUtils.getFile("http:/opt/234.json");

    }

    //org.springframework.core.io.ClassPathResource
    @Test
    public void test2() throws Exception {

//        path2 = file:/D:/code/axinger/ax-springboot2-demo/a01-spring-boot-learn/a01-01-spring-boot-mvc/target/classes/my.yml
//        path3 = D:\code\axinger\ax-springboot2-demo\a01-spring-boot-learn\a01-01-spring-boot-mvc\target\classes\my.yml
//        ClassPathResource resource = new ClassPathResource("my.yml");
        ClassPathResource resource = new ClassPathResource("123.json");
        String path2 = resource.getURL().toString();
        String filename = resource.getFilename();
        System.out.println("filename = " + filename);
        System.out.println("path2 = " + path2);

        File file = resource.getFile();
        String path3 = file.getAbsolutePath();
        System.out.println("path3 = " + path3);
        String read = FileUtil.readString(file, StandardCharsets.UTF_8);
        System.out.println("read = " + read);

        InputStream inputStream = resource.getInputStream();
        System.out.println("inputStream = " + inputStream);
        byte[] fileData = FileCopyUtils.copyToByteArray(inputStream);
        String val = new String(fileData, StandardCharsets.UTF_8);
        System.out.println("inputStream读取内容 = " + val);
    }


    //cn.hutool.core.io.resource
    @Test
    public void test4() {
        String content = ResourceUtil.readUtf8Str("123.json");

        System.out.println("content = " + content);

        //当资源不存在时返回null
        InputStream inputStream = ResourceUtil.getStreamSafe("123.json");
        System.out.println("inputStream = " + inputStream);

        URL url = ResourceUtil.getResource("123.json");
        System.out.println("url = " + url);

        List<URL> urlList = ResourceUtil.getResources("dir1/");
        System.out.println("urlList = " + urlList);

        Resource resource = ResourceUtil.getResourceObj("123.json");
        URL url1 = resource.getUrl();
        System.out.println("url1 = " + url1);
    }
}
