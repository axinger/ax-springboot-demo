package com.github.axinger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunJavaScript {
    public static void main(String[] args) {
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        // 定义要执行的命令
        String nodePath = "node"; // Node.js 的路径（如果已添加到环境变量，直接写 "node" 即可）
        String jsFilePath = "test.js"; // 替换为你的 JavaScript 文件路径

        // 创建 ProcessBuilder 对象
        ProcessBuilder processBuilder = new ProcessBuilder(nodePath, jsFilePath, "arg1", "arg2");

        try {
            // 启动进程
            Process process = processBuilder.start();

            // 读取输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
