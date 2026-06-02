package com.github.axinger;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

class CommonsCompressTest {

    @Test
    void testZipCreateAndRead() throws IOException {
        File zipFile = new File(System.getProperty("java.io.tmpdir"), "test.zip");

        // ========== 创建 ZIP 文件 ==========
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipArchiveOutputStream zos = new ZipArchiveOutputStream(fos)) {

            // 添加第一个文件
            ZipArchiveEntry entry1 = new ZipArchiveEntry("file1.txt");
            zos.putArchiveEntry(entry1);
            zos.write("Hello Zip!".getBytes(StandardCharsets.UTF_8));
            zos.closeArchiveEntry();

            // 添加第二个文件（带目录）
            ZipArchiveEntry entry2 = new ZipArchiveEntry("docs/readme.txt");
            zos.putArchiveEntry(entry2);
            zos.write("This is a readme file.\n第二行内容".getBytes(StandardCharsets.UTF_8));
            zos.closeArchiveEntry();
        }

        System.out.println("ZIP 文件创建成功: " + zipFile.getAbsolutePath());

        // ========== 读取 ZIP 文件 ==========
        try (ZipFile zf = new ZipFile(zipFile)) {
            Enumeration<ZipArchiveEntry> entries = zf.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                System.out.println("条目: " + entry.getName() + " 大小: " + entry.getSize());

                // 读取内容
                try (InputStream is = zf.getInputStream(entry);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    is.transferTo(baos);
                    System.out.println("内容: " + baos.toString(StandardCharsets.UTF_8));
                }
            }
        }

        // 清理
        zipFile.delete();
    }

    @Test
    void testZipStreamRead() throws IOException {
        // 使用流方式读取 ZIP（适合大文件，不需要随机访问）
        File zipFile = new File(System.getProperty("java.io.tmpdir"), "stream-test.zip");

        // 先创建
        try (ZipArchiveOutputStream zos = new ZipArchiveOutputStream(new FileOutputStream(zipFile))) {
            ZipArchiveEntry entry = new ZipArchiveEntry("data.txt");
            zos.putArchiveEntry(entry);
            zos.write("Stream reading test".getBytes(StandardCharsets.UTF_8));
            zos.closeArchiveEntry();
        }

        // 流式读取
        try (ZipArchiveInputStream zis = new ZipArchiveInputStream(new FileInputStream(zipFile))) {
            ZipArchiveEntry entry;
            while ((entry = zis.getNextZipEntry()) != null) {
                System.out.println("读取条目: " + entry.getName());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    System.out.print(new String(buffer, 0, len, StandardCharsets.UTF_8));
                }
                System.out.println();
            }
        }

        zipFile.delete();
    }
}
