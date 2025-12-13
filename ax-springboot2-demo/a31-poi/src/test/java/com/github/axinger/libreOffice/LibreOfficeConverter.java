package com.github.axinger.libreOffice;

import java.io.File;
import java.io.IOException;

public class LibreOfficeConverter {

    public static void main(String[] args) {
        // 输入 PDF 文件路径
        String inputFilePath = "D:\\test01.pdf";

        // 输出文件夹路径
        String outputFolderPath = "D:\\test01Dir";

        // 调用 LibreOffice 进行转换
        try {
            convertToJpg(inputFilePath, outputFolderPath);
            System.out.println("转换完成！");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("转换失败: " + e.getMessage());
        }
    }

    /**
     * 调用 LibreOffice 将 PDF 转换为 JPG
     *
     * @param inputFilePath    输入文件路径
     * @param outputFolderPath 输出文件夹路径
     */
    public static void convertToJpg(String inputFilePath, String outputFolderPath)
            throws IOException, InterruptedException {
        // 确保输出文件夹存在
        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
        }

        // 构建命令
        String command;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // Windows系统下的命令，使用完整路径
            // 构建命令（Windows）
            command = String.format(
                    "\"C:\\Program Files\\LibreOffice\\program\\soffice.exe\" --headless --convert-to jpg --outdir %s %s",
                    outputFolderPath, inputFilePath
            );
        } else {
            // Linux/Mac系统下的命令  libreoffice --headless --convert-to jpg
            command = String.format(
                    "/usr/bin/libreoffice --headless --convert-to jpg --outdir %s %s",
                    outputFolderPath, inputFilePath
            );
        }


        // 执行命令
        Process process = Runtime.getRuntime().exec(command);

        // 等待命令执行完成
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("转换成功！");
        } else {
            System.err.println("转换失败，退出码: " + exitCode);
        }
    }
}
