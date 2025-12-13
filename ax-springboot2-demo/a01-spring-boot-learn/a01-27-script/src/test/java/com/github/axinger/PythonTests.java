package com.github.axinger;

import cn.hutool.core.io.resource.ResourceUtil;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class PythonTests {

    /*

在 Spring Boot 中，ResourceUtils.getFile() 和 ClassPathResource 获取文件路径的方式有以下区别：

1. ResourceUtils.getFile("classpath:test01.py")
返回的是标准的 java.io.File 对象

getAbsolutePath() 返回的是本地文件系统的绝对路径（Windows 下带盘符）

示例输出：D:\code\...\target\classes\test01.py

缺点：当应用打包为 JAR 文件运行时，这种方法会失败，因为 JAR 中的资源不是独立的文件

2. new ClassPathResource("test01.py")
是 Spring 提供的专门用于类路径资源访问的类

getURL().getPath() 返回的是 URL 编码的路径（以 / 开头）

示例输出：/D:/code/.../target/classes/test01.py

优点：在 JAR 包中也能工作，因为它不依赖于文件系统路径
     */
    @Test
    public void test01() {
        // 要传递的两个数字参数
        String num1 = "10";
        String num2 = "3";

        try {
            /// 可用
            File file = ResourceUtils.getFile("classpath:py/test01.py");
            String path1 = file.getAbsolutePath();
            System.out.println("path1 = " + path1);

            /// 不可用
            ClassPathResource resource = new ClassPathResource("py/test01.py");
            String path = resource.getURL().getPath();
            System.out.println("path = " + path);

            /// 可用
            String path2 = resource.getFile().getAbsolutePath();
            System.out.println("path2 = " + path2);

            // 创建ProcessBuilder执行Python脚本
            java.lang.ProcessBuilder pb = new java.lang.ProcessBuilder("python", path2, num1, num2);
            Process process = pb.start();

            // 读取Python脚本的输出
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Python输出: " + line);

                // 这里可以添加JSON解析代码来处理返回的复杂数据
                // 例如使用Jackson或Gson库解析JSON
            }

            int exitCode = process.waitFor();
            System.out.println("Python脚本执行结束，退出码: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    void test02() {
        String num1 = "10";
        String num2 = "3";

        try {
            ClassPathResource resource = new ClassPathResource("test01.py");

            // 创建临时文件
            File tempFile = File.createTempFile("script", ".py");
            try (InputStream in = resource.getInputStream();
                 FileOutputStream out = new FileOutputStream(tempFile)) {
                FileCopyUtils.copy(in, out);
            }

            // 设置临时文件在程序退出时删除
            tempFile.deleteOnExit();

            // 执行 Python 脚本
            ProcessBuilder pb = new ProcessBuilder("python", tempFile.getAbsolutePath(), num1, num2);
            Process process = pb.start();

            // 读取输出
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("Python输出: " + line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Python脚本执行结束，退出码: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test03() {
// 创建支持多语言的上下文
//        try (Context context = Context.create("python")) {
//            Value result = context.eval("python", "2 + 3 * 4");
//            System.out.println(result.asInt()); // 14
//
//        }
        String data = ResourceUtil.readUtf8Str("py/test02.py");
        try (Context context = Context.create("python")) {
            context.eval("python", data);
            Value value1 = context.getBindings("python").getMember("add");
            if (value1 != null && value1.canExecute()) {
                Value sumResult = value1.execute(1, 2);
                System.out.println("Java端获取的,add:结果: " + sumResult.asInt());
            }
        } catch (Exception e) {
            System.err.println("执行错误 = " + e);
        }
    }
}


