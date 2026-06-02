package com.github.axinger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

class CommonsIOTest {

    // ==================== FileUtils ====================

    @Test
    void testFileUtils_readWrite() throws IOException {
        File file = new File(SystemUtils.getJavaIoTmpDir(), "commons-io-test.txt");

        // 写入字符串到文件
        FileUtils.writeStringToFile(file, "Hello Commons IO!\n第二行", StandardCharsets.UTF_8);

        // 读取文件为字符串
        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        System.out.println("文件内容:\n" + content);

        // 读取为行列表
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        lines.forEach(System.out::println);

        // 追加内容
        FileUtils.writeStringToFile(file, "\n追加的内容", StandardCharsets.UTF_8, true);

        // 复制文件
        File copy = new File(SystemUtils.getJavaIoTmpDir(), "commons-io-test-copy.txt");
        FileUtils.copyFile(file, copy);

        // 删除文件
        FileUtils.deleteQuietly(copy);
        FileUtils.deleteQuietly(file);
    }

    @Test
    void testFileUtils_directory() throws IOException {
        File dir = new File(SystemUtils.getJavaIoTmpDir(), "commons-io-dir");
        File subDir = new File(dir, "sub");

        // 创建目录（包括父目录）
        FileUtils.forceMkdir(subDir);

        // 创建空文件
        File newFile = new File(subDir, "test.txt");
        FileUtils.touch(newFile);

        // 写入数据
        FileUtils.writeStringToFile(newFile, "test data", StandardCharsets.UTF_8);

        // 获取目录大小
        long size = FileUtils.sizeOfDirectory(dir);
        System.out.println("目录大小: " + size + " bytes");

        // 清空目录内容（保留目录本身）
        FileUtils.cleanDirectory(dir);

        // 删除目录及内容
        FileUtils.deleteDirectory(dir);
    }

    @Test
    void testFileUtils_copyDirectory() throws IOException {
        File srcDir = new File(SystemUtils.getJavaIoTmpDir(), "src-dir");
        File destDir = new File(SystemUtils.getJavaIoTmpDir(), "dest-dir");

        FileUtils.forceMkdir(srcDir);
        FileUtils.writeStringToFile(new File(srcDir, "a.txt"), "a", StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(srcDir, "b.txt"), "b", StandardCharsets.UTF_8);

        // 复制整个目录
        FileUtils.copyDirectory(srcDir, destDir);

        // 复制目录，只复制 .txt 文件
        File filteredDest = new File(SystemUtils.getJavaIoTmpDir(), "filtered-dir");
        FileUtils.copyDirectory(srcDir, filteredDest, new SuffixFileFilter(".txt"));

        // 清理
        FileUtils.deleteDirectory(srcDir);
        FileUtils.deleteDirectory(destDir);
        FileUtils.deleteDirectory(filteredDest);
    }

    @Test
    void testFileUtils_listFiles() throws IOException {
        File dir = new File(SystemUtils.getJavaIoTmpDir(), "list-test");
        FileUtils.forceMkdir(dir);
        FileUtils.writeStringToFile(new File(dir, "a.txt"), "a", StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(dir, "b.log"), "b", StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(dir, "c.txt"), "c", StandardCharsets.UTF_8);

        // 列出所有 .txt 文件（递归）
        Collection<File> txtFiles = FileUtils.listFiles(dir, new String[]{"txt"}, true);
        txtFiles.forEach(f -> System.out.println("找到: " + f.getName()));

        // 列出所有文件（递归）
        Collection<File> allFiles = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        allFiles.forEach(f -> System.out.println("所有文件: " + f.getName()));

        FileUtils.deleteDirectory(dir);
    }

    // ==================== FilenameUtils ====================

    @Test
    void testFilenameUtils() {
        String path = "D:/data/file.txt";

        // 获取文件名
        System.out.println(FilenameUtils.getName(path));        // file.txt

        // 获取基础名（不含扩展名）
        System.out.println(FilenameUtils.getBaseName(path));    // file

        // 获取扩展名
        System.out.println(FilenameUtils.getExtension(path));   // txt

        // 获取完整路径（不含文件名）
        System.out.println(FilenameUtils.getFullPath(path));    // D:/data/

        // 路径拼接
        System.out.println(FilenameUtils.concat("/home/user", "docs/file.txt")); // /home/user/docs/file.txt

        // 路径规范化
        System.out.println(FilenameUtils.normalize("/home/user/../docs/./file.txt")); // /home/docs/file.txt

        // 判断扩展名
        System.out.println(FilenameUtils.isExtension(path, "txt"));  // true
        System.out.println(FilenameUtils.isExtension(path, "jpg", "png", "txt")); // true

        // 移除扩展名
        System.out.println(FilenameUtils.removeExtension(path)); // D:/data/file

        // 路径分隔符转换
        System.out.println(FilenameUtils.separatorsToUnix("D:\\data\\file.txt")); // D:/data/file.txt
    }

    // ==================== IOUtils ====================

    @Test
    void testIOUtils() throws IOException {
        // 将 InputStream 转为字符串
        InputStream is = new ByteArrayInputStream("Hello IOUtils".getBytes(StandardCharsets.UTF_8));
        String content = IOUtils.toString(is, StandardCharsets.UTF_8);
        System.out.println(content);

        // 将字符串写入 OutputStream
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IOUtils.write("Hello Output", os, StandardCharsets.UTF_8);
        System.out.println(os.toString(StandardCharsets.UTF_8));

        // 复制流
        InputStream source = new ByteArrayInputStream("copy me".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        long copiedBytes = IOUtils.copy(source, target);
        System.out.println("复制了 " + copiedBytes + " bytes");
        System.out.println(target.toString(StandardCharsets.UTF_8));

        // 读取为字节数组
        InputStream bytesIs = new ByteArrayInputStream("byte array".getBytes(StandardCharsets.UTF_8));
        byte[] bytes = IOUtils.toByteArray(bytesIs);
        System.out.println(new String(bytes, StandardCharsets.UTF_8));

        // 读取为行列表
        InputStream linesIs = new ByteArrayInputStream("line1\nline2\nline3".getBytes(StandardCharsets.UTF_8));
        List<String> lines = IOUtils.readLines(linesIs, StandardCharsets.UTF_8);
        lines.forEach(System.out::println);

        // 静默关闭流
        IOUtils.closeQuietly(is);
    }

    // 辅助类，用于获取临时目录
    private static class SystemUtils {
        static File getJavaIoTmpDir() {
            return new File(System.getProperty("java.io.tmpdir"));
        }
    }
}
