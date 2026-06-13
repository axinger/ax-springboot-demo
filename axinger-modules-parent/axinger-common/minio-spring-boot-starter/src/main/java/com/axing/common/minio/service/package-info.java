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
 * MinIO 服务层
 *
 * <p>该包包含 MinIO 核心服务的接口定义和实现，提供完整的文件操作能力。</p>
 *
 * <h2>核心接口：</h2>
 * <ul>
 *   <li>{@link com.axing.common.minio.service.MinioTemplate} - MinIO 操作模板接口</li>
 *   <li>{@link com.axing.common.minio.service.impl.MinioTemplateImpl} - MinIO 操作模板实现</li>
 * </ul>
 *
 * <h2>接口方法分类：</h2>
 *
 * <h3>存储桶操作：</h3>
 * <ul>
 *   <li>{@code bucketExists()} - 检查存储桶是否存在</li>
 *   <li>{@code createBucket()} - 创建存储桶</li>
 *   <li>{@code removeBucket()} - 删除存储桶</li>
 *   <li>{@code getBuckets()} - 获取所有存储桶列表</li>
 *   <li>{@code getBucket()} - 获取存储桶信息</li>
 * </ul>
 *
 * <h3>文件上传：</h3>
 * <ul>
 *   <li>{@code uploadFile()} - 上传 MultipartFile 文件</li>
 *   <li>{@code uploadStream()} - 上传输入流</li>
 *   <li>{@code createUploadUrl()} - 创建预签名上传 URL</li>
 *   <li>{@code createUploadChunkUrlList()} - 创建分片上传 URL 列表</li>
 *   <li>{@code createUploadChunkUrl()} - 创建单个分片上传 URL</li>
 * </ul>
 *
 * <h3>文件下载：</h3>
 * <ul>
 *   <li>{@code download()} - 传统方式下载文件</li>
 *   <li>{@code downloadStreaming()} - 流式下载</li>
 *   <li>{@code writeToPath()} - 下载到本地路径</li>
 *   <li>{@code fileUrl()} - 获取文件访问 URL</li>
 * </ul>
 *
 * <h3>文件管理：</h3>
 * <ul>
 *   <li>{@code list()} - 列出文件</li>
 *   <li>{@code getItemsByPrefix()} - 按前缀查询文件</li>
 *   <li>{@code deleteObjectName()} - 删除单个文件</li>
 *   <li>{@code deleteObjectNames()} - 批量删除文件</li>
 *   <li>{@code copyObject()} - 复制文件</li>
 *   <li>{@code composeObject()} - 合并分片文件</li>
 *   <li>{@code listObjectNames()} - 列出分片文件</li>
 *   <li>{@code mapChunkObjectNames()} - 获取分片映射</li>
 * </ul>
 *
 * <h2>使用示例：</h2>
 * <pre>{@code
 * @Service
 * public class FileService {
 *
 *     @Autowired
 *     private MinioTemplate minioTemplate;
 *
 *     public List<Item> listFiles(String bucketName) {
 *         return minioTemplate.list(bucketName);
 *     }
 *
 *     public UploadFileBO uploadFile(MultipartFile file) {
 *         return minioTemplate.uploadFile(file, "default-bucket");
 *     }
 *
 *     public void downloadFile(String fileName, HttpServletResponse response) {
 *         minioTemplate.download(response, "default-bucket", fileName);
 *     }
 * }
 * }</pre>
 *
 * <h2>实现原理：</h2>
 * <p>MinioTemplateImpl 使用 MinIO Java SDK 实现所有方法，提供：</p>
 * <ul>
 *   <li>连接池管理和复用</li>
 *   <li>异常统一处理</li>
 *   <li>性能优化</li>
 *   <li>线程安全</li>
 * </ul>
 *
 * <p>所有方法都支持同步操作，如需异步使用，请在调用层封装。</p>
 *
 * @author Axinger Team
 * @version 1.0.0
 * @see com.axing.common.minio.service.MinioTemplate
 * @see com.axing.common.minio.service.impl.MinioTemplateImpl
 */
package com.axing.common.minio.service;