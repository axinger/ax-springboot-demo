package com.github.axinger;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.net.URL;

public class ResourceUtilTests {

    @Test
    public void test01() {

        String name = "123.json";


        ClassPathResource resource = new ClassPathResource(name);
        String path = resource.getPath();
        System.out.println("path = " + path);
//
        URL url = ResourceUtil.getResource(name);
        String path1 = url.getPath();
        String file1 = url.getFile();
        System.out.println("file1 = " + file1);
        String path11 = url.getFile();
        System.out.println("path1 = " + path1);
        System.out.println("path11 = " + path11);
        Resource resourceObj = ResourceUtil.getResourceObj(name);
        System.out.println("resourceObj = " + resourceObj.getUrl().getFile());

        String absolutePath = FileUtil.getAbsolutePath(path1);
        System.out.println("absolutePath = " + absolutePath);

        boolean file = FileUtil.isFile(path1);
        System.out.println("file = " + file);

        String path2 = "D:/opt/test.js";
        System.out.println("path2 = " + path2);

        boolean file2 = FileUtil.isFile(path2);
        System.out.println("file2 = " + file2);

    }

}
