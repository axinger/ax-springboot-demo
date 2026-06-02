package com.github.axinger;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Apache Commons FileUpload 文件上传示例
 * 
 * 【与 Spring 内置文件上传对比】
 * 
 * | 特性 | Commons FileUpload | Spring MultipartFile |
 * |------|-------------------|---------------------|
 * | 依赖 | 需额外引入 commons-fileupload | Spring 内置，无需额外依赖 |
 * | 配置方式 | 手动创建 ServletFileUpload、配置 DiskFileItemFactory | 通过 application.yml/properties 配置 |
 * | 使用场景 | 原生 Servlet/JSP 项目、需要精细控制上传过程 | Spring Boot/Spring MVC 项目 |
 * | 代码复杂度 | 需要手动解析请求、处理 FileItem | 直接注入 MultipartFile，API 更简洁 |
 * | 进度监听 | 支持 ProgressListener | 需自定义实现 |
 * | 临时文件管理 | 手动配置仓库目录和清理 | Spring 自动管理 |
 * 
 * 【Spring Boot 配置示例】
 * ```yaml
 * spring:
 *   servlet:
 *     multipart:
 *       enabled: true
 *       max-file-size: 10MB
 *       max-request-size: 50MB
 *       file-size-threshold: 1MB
 *       location: /tmp/upload
 * ```
 * 
 * 【Spring 控制器示例】
 * ```java
 * @PostMapping("/upload")
 * public String upload(@RequestParam("file") MultipartFile file) {
 *     String fileName = file.getOriginalFilename();
 *     long size = file.getSize();
 *     String contentType = file.getContentType();
 *     
 *     // 保存文件
 *     file.transferTo(new File("/path/to/dest/" + fileName));
 *     
 *     return "上传成功";
 * }
 * ```
 * 
 * 【建议】
 * - Spring 项目：直接使用 Spring 内置的 MultipartFile，更简单
 * - 非 Spring 的 Servlet 项目：使用 Commons FileUpload
 * - 需要上传进度条等特殊需求：Commons FileUpload 更灵活
 */
class CommonsFileUploadTest {

    /**
     * 模拟 HTTP 请求输入流（multipart/form-data 格式）
     */
    private InputStream createMockMultipartStream(String boundary) {
        StringBuilder sb = new StringBuilder();
        // 文本字段
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"username\"\r\n\r\n");
        sb.append("Alice\r\n");

        // 文件字段
        sb.append("--").append(boundary).append("\r\n");
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"test.txt\"\r\n");
        sb.append("Content-Type: text/plain\r\n\r\n");
        sb.append("Hello FileUpload!\r\n");
        sb.append("This is a test file content.\r\n");

        // 结束标记
        sb.append("--").append(boundary).append("--\r\n");

        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testParseUpload() throws Exception {
        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";

        // 创建工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存阈值（超过则写入临时文件）
        factory.setSizeThreshold(1024 * 1024); // 1MB
        // 设置临时目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        // 创建上传处理器
        ServletFileUpload upload = new ServletFileUpload(factory);
        // 设置最大文件大小
        upload.setFileSizeMax(10 * 1024 * 1024); // 10MB
        // 设置最大请求大小
        upload.setSizeMax(50 * 1024 * 1024); // 50MB
        // 设置编码
        upload.setHeaderEncoding("UTF-8");

        // 模拟解析请求
        InputStream input = createMockMultipartStream(boundary);
        String contentType = "multipart/form-data; boundary=" + boundary;

        List<FileItem> items = upload.parseRequest(new MockRequest(input, contentType));

        for (FileItem item : items) {
            if (item.isFormField()) {
                // 普通表单字段
                System.out.println("字段名: " + item.getFieldName());
                System.out.println("字段值: " + item.getString("UTF-8"));
            } else {
                // 文件字段
                System.out.println("字段名: " + item.getFieldName());
                System.out.println("文件名: " + item.getName());
                System.out.println("文件大小: " + item.getSize() + " bytes");
                System.out.println("Content-Type: " + item.getContentType());
                System.out.println("文件内容: " + item.getString("UTF-8"));
            }
        }
    }

    @Test
    void testFileItemOperations() throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        String boundary = "----TestBoundary";
        InputStream input = createMockMultipartStream(boundary);
        String contentType = "multipart/form-data; boundary=" + boundary;

        List<FileItem> items = upload.parseRequest(new MockRequest(input, contentType));

        for (FileItem item : items) {
            if (!item.isFormField()) {
                // 获取文件信息
                System.out.println("是否内存中: " + item.isInMemory());
                System.out.println("输入流可用: " + (item.getInputStream() != null));

                // 写入到指定路径
                File destFile = new File(System.getProperty("java.io.tmpdir"), "uploaded_" + item.getName());
                item.write(destFile);
                System.out.println("已写入: " + destFile.getAbsolutePath());

                // 删除临时文件
                item.delete();
            }
        }
    }

    /**
     * 模拟 HttpServletRequest
     */
    static class MockRequest implements org.apache.commons.fileupload.RequestContext {
        private final InputStream inputStream;
        private final String contentType;

        public MockRequest(InputStream inputStream, String contentType) {
            this.inputStream = inputStream;
            this.contentType = contentType;
        }

        @Override
        public String getCharacterEncoding() {
            return "UTF-8";
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public int getContentLength() {
            try {
                return inputStream.available();
            } catch (IOException e) {
                return -1;
            }
        }

        @Override
        public InputStream getInputStream() {
            return inputStream;
        }
    }
}
