/*
 * Copyright (c) 2024 Axinger. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * MinIO 对象存储 Spring Boot 启动器
 *
 * <p>该模块提供了完整的 MinIO 对象存储集成解决方案，包括：</p>
 *
 * <ul>
 *   <li><b>自动配置</b> - Spring Boot 自动配置支持</li>
 *   <li><b>文件操作</b> - 上传、下载、删除、复制等完整功能</li>
 *   <li><b>分片上传</b> - 大文件分片上传和合并</li>
 *   <li><b>批量操作</b> - 支持批量文件管理</li>
 *   <li><b>URL 生成</b> - 预签名 URL 和访问链接</li>
 *   <li><b>异常处理</b> - 统一的异常处理机制</li>
 * </ul>
 *
 * <h2>架构设计：</h2>
 * <pre>
 * ┌─────────────────┐
 * │   MinioTemplate  │ ← 核心接口
 * │   (service)      │
 * └────────┬────────┘
 *          │
 * ┌────────▼────────┐
 * │ MinioTemplateImpl│ ← 实现层
 * │   (service/impl) │
 * └────────┬────────┘
 *          │
 * ┌────────▼────────┐
 * │   MinioClient    │ ← MinIO SDK
 * │   (第三方库)     │
 * └─────────────────┘
 * </pre>
 *
 * <h2>主要包结构：</h2>
 * <ul>
 *   <li>{@link com.axing.common.minio.bean} - 配置属性类，定义 MinIO 连接参数</li>
 *   <li>{@link com.axing.common.minio.config} - 自动配置类，注册 MinIO 相关 Bean</li>
 *   <li>{@link com.axing.common.minio.error} - 自定义异常类</li>
 *   <li>{@link com.axing.common.minio.model} - 数据传输对象</li>
 *   <li>{@link com.axing.common.minio.service} - 核心服务接口</li>
 *   <li>{@link com.axing.common.minio.service.impl} - 服务实现类</li>
 *   <li>{@link com.axing.common.minio.util} - 工具类</li>
 * </ul>
 *
 * <h2>快速开始：</h2>
 * <h3>1. 添加依赖：</h3>
 * <pre>{@code
 * <dependency>
 *     <groupId>com.axinger</groupId>
 *     <artifactId>minio-spring-boot-starter</artifactId>
 *     <version>1.0.0</version>
 * </dependency>
 * }</pre>
 *
 * <h3>2. 配置文件：</h3>
 * <pre>{@code
 * # application.yml
 * minio:
 *   endpoint: http://localhost:9000
 *   accessKey: your-access-key
 *   secretKey: your-secret-key
 *   bucketName: my-bucket
 * }</pre>
 *
 * <h3>3. 使用示例：</h3>
 * <pre>{@code
 * @RestController
 * @RequestMapping("/file")
 * public class FileController {
 *
 *     @Autowired
 *     private MinioTemplate minioTemplate;
 *
 *     @PostMapping("/upload")
 *     public Result upload(@RequestParam("file") MultipartFile file) {
 *         UploadFileBO result = minioTemplate.uploadFile(file, "my-bucket");
 *         return Result.success(result);
 *     }
 *
 *     @GetMapping("/download/{fileName}")
 *     public void download(@PathVariable String fileName,
 *                          HttpServletResponse response) {
 *         minioTemplate.download(response, "my-bucket", fileName);
 *     }
 *
 *     @DeleteMapping("/delete/{fileName}")
 *     public Result delete(@PathVariable String fileName) {
 *         boolean success = minioTemplate.deleteObjectName("my-bucket", fileName);
 *         return Result.success(success);
 *     }
 * }
 * }</pre>
 *
 * <h2>高级功能：</h2>
 * <h3>分片上传：</h3>
 * <pre>{@code
 * // 1. 初始化分片上传
 * List<String> chunkUrls = minioTemplate.createUploadChunkUrlList(
 *     "bucket", "file-md5", 5, 3600);
 *
 * // 2. 上传分片到返回的 URL
 * // ... 上传逻辑 ...
 *
 * // 3. 合并分片
 * minioTemplate.composeObject("chunk-bucket", "target-bucket",
 *     chunkNames, "final-file.txt");
 * }</pre>
 *
 * <h3>获取访问 URL：</h3>
 * <pre>{@code
 * // 获取临时访问 URL
 * String url = minioTemplate.fileUrl("bucket", "file.txt");
 *
 * // 创建上传 URL
 * String uploadUrl = minioTemplate.createUploadUrl("bucket", "file.txt", 3600);
 * }</pre>
 *
 * <h2>配置属性：</h2>
 * <ul>
 *   <li>{@code minio.endpoint} - MinIO 服务地址 (必需)</li>
 *   <li>{@code minio.accessKey} - 访问密钥 (必需)</li>
 *   <li>{@code minio.secretKey} - 秘密密钥 (必需)</li>
 *   <li>{@code minio.bucketName} - 默认存储桶名称</li>
 *   <li>{@code minio.region} - 区域设置</li>
 * </ul>
 *
 * <h2>异常处理：</h2>
 * <p>所有 MinIO 操作异常都会被转换为 {@link com.axing.common.minio.error.MinioException}，
 * 包含详细的错误信息和错误码。</p>
 *
 * <h2>性能优化：</h2>
 * <ul>
 *   <li>支持连接池配置</li>
 *   <li>分片上传优化大文件传输</li>
 *   <li>批量操作减少网络开销</li>
 *   <li>流式下载节省内存</li>
 * </ul>
 *
 * <p>该模块完全遵循 Spring Boot 设计理念，提供了开箱即用的 MinIO 集成方案。</p>
 *
 * @author Axinger Team
 * @version 1.0.0
 * @since 2024-01-01
 * @see <a href="https://min.io/">MinIO Official Website</a>
 * @see <a href="https://github.com/minio/minio-java">MinIO Java SDK</a>
 */
package com.axing.common.minio;