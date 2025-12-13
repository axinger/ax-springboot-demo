package com.github.axinger;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProcessBuilderTests {

    @Test
    public void test01() {
        URL url = ResourceUtil.getResource("js/test.js");
        String absolutePath = FileUtil.getAbsolutePath(url.getPath());


        // 绝对路径
        ProcessBuilder pb = new ProcessBuilder("node", absolutePath);

        // 可选：设置工作目录（比如 JS 文件所在目录）
//        pb.directory(new File("/opt"));

        // 合并错误输出到标准输出，方便一起读取
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();

            // 读取 JS 输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("每一行的log: " + line);
            }

            // 等待执行完成
            int exitCode = process.waitFor();
            System.out.println("退出获取值: " + exitCode);

        } catch (Exception e) {

            System.err.println("e = " + e);
        }
    }


    @Test
    public void test02() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "李四");
        params.put("age", 30);
        params.put("hobbies", Arrays.asList("阅读", "游泳", "编程"));
        params.put("metadata", Map.of("version", "1.0", "active", true));

        // 使用Jackson ObjectMapper确保标准JSON格式
        String json = JSON.toJSONString(params);

        // 转义双引号以适应命令行参数传递
        json = json.replace("\"", "\\\"");
        URL url = ResourceUtil.getResource("py/test03.py");
        String absolutePath = FileUtil.getAbsolutePath(url.getPath());
        ProcessBuilder pb = new ProcessBuilder("python", absolutePath, json);
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("每一行的log: " + line);
            }

            // 等待执行完成
            int exitCode = process.waitFor();
            System.out.println("退出获取值: " + exitCode);
        } catch (Exception e) {

            System.err.println("e = " + e);
        }
    }
}
