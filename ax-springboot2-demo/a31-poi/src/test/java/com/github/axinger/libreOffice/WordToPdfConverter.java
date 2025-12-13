package com.github.axinger.libreOffice;

import java.io.File;
import java.io.IOException;

public class WordToPdfConverter {

    /// 复杂的word效果好
    public static void main(String[] args) {
        // 输入Word文件路径
        String inputFilePath = "D:\\test01.docx";
        File inputFile = new File(inputFilePath);

        // 输出目录路径
        String outputDirPath = "output"; // 请确保该目录存在或在代码中创建它
        File outputDir = new File(outputDirPath);

        if (!inputFile.exists()) {
            System.err.println("输入文件不存在: " + inputFilePath);
            return;
        }

        if (!outputDir.exists() && !outputDir.mkdirs()) {
            System.err.println("无法创建输出目录: " + outputDirPath);
            return;
        }

        try {
            // 根据操作系统调整命令
            String command;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Windows系统下的命令，使用完整路径
                command = "\"C:\\Program Files\\LibreOffice\\program\\soffice.exe\" --headless --convert-to pdf --outdir \"" + outputDirPath + "\" \"" + inputFilePath + "\"";
            } else {
                // Linux/Mac系统下的命令
                command = "soffice --headless --convert-to pdf --outdir \"" + outputDirPath + "\" \"" + inputFilePath + "\"";
            }

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);

            // 读取命令的输出和错误信息，防止阻塞
            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");

            // 启动线程读取流
            errorGobbler.start();
            outputGobbler.start();

            // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("转换成功！PDF文件位置: " + outputDir.getAbsolutePath());
            } else {
                System.err.println("转换失败！");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 内部类用于读取子进程的输出流和错误流
    private static class StreamGobbler extends Thread {
        private final java.io.InputStream inputStream;
        private final String name;

        public StreamGobbler(java.io.InputStream inputStream, String name) {
            this.inputStream = inputStream;
            this.name = name;
        }

        @Override
        public void run() {
            java.util.Scanner sc = new java.util.Scanner(inputStream).useDelimiter("\\A");
            if (sc.hasNext()) {
                System.out.println("[" + name + "] " + sc.next());
            }
            sc.close();
        }
    }
}
