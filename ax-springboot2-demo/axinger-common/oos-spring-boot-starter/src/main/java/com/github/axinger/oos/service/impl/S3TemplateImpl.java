package com.github.axinger.oos.service.impl;

import com.github.axinger.oos.dto.S3ObjectSummary;
import com.github.axinger.oos.service.S3Template;
import com.github.axinger.oos.util.S3Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.IoUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public record S3TemplateImpl(S3Client s3Client, S3Presigner s3Presigner) implements S3Template {

    /**
     * 判断Bucket是否存在
     */
    private boolean bucketNotExists(String bucketName) {
        return !bucketExists(bucketName);
    }

    private boolean bucketExists(String bucketName) {
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
            return true;
        } catch (NoSuchBucketException exception) {
            return false;
        } catch (S3Exception e) {
            log.error("检查Bucket存在性失败: {}", bucketName, e);
            return false;
        }
    }

    /**
     * 创建Bucket
     */
    @SneakyThrows
    @Override
    public void createBucket(String bucketName) {
        if (bucketNotExists(bucketName)) {
            try {
                // 创建Bucket
                CreateBucketResponse response = s3Client.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket创建成功: {}", bucketName);

                // 添加Bucket的访问策略

                String policy = S3Util.createBucketPolicyWithJackson(bucketName);

                setBucketPolicy(bucketName, policy);
            } catch (BucketAlreadyExistsException e) {
                log.warn("Bucket已存在: {}", bucketName);
            } catch (S3Exception e) {
                log.error("创建Bucket失败: {}", bucketName, e);
                throw e;
            }
        } else {
            log.info("Bucket已存在，跳过创建: {}", bucketName);
        }
    }

    /**
     * 获取所有的buckets
     */
    @SneakyThrows
    @Override
    public List<Bucket> getAllBuckets() {
        try {
            return s3Client.listBuckets().buckets();
        } catch (S3Exception e) {
            log.error("获取Bucket列表失败", e);
            throw e;
        }
    }

    /**
     * 通过Bucket名称删除Bucket
     */
    @SneakyThrows
    @Override
    public void removeBucket(String bucketName) {
        if (bucketNotExists(bucketName)) {
            log.warn("Bucket不存在，跳过删除: {}", bucketName);
            return;
        }

        try {
            // 先清空Bucket中的对象
            emptyBucket(bucketName);

            // 删除Bucket
            s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
            log.info("Bucket删除成功: {}", bucketName);
        } catch (S3Exception e) {
            log.error("删除Bucket失败: {}", bucketName, e);
            throw e;
        }
    }

    /**
     * 清空Bucket中的所有对象
     */
    @SneakyThrows
    public void emptyBucket(String bucketName) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsV2Response response;
            do {
                response = s3Client.listObjectsV2(request);

                if (response.contents() != null && !response.contents().isEmpty()) {
                    // 批量删除对象
                    List<ObjectIdentifier> objectsToDelete = response.contents().stream()
                            .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                            .collect(Collectors.toList());

                    DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                            .bucket(bucketName)
                            .delete(Delete.builder().objects(objectsToDelete).build())
                            .build();

                    s3Client.deleteObjects(deleteRequest);
                    log.info("清空Bucket对象: {}，删除{}个对象", bucketName, objectsToDelete.size());
                }

                request = request.toBuilder().continuationToken(response.nextContinuationToken()).build();
            } while (response.isTruncated());
        } catch (S3Exception e) {
            log.error("清空Bucket失败: {}", bucketName, e);
            throw e;
        }
    }

    /**
     * 上传对象
     */
    @SneakyThrows
    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream, String contextType) {
        return putObject(bucketName, objectName, stream, stream.available(), contextType);
    }

    /**
     * 上传对象
     */
    @SneakyThrows
    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream) {
        return putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 上传MultipartFile文件
     */
    @SneakyThrows
    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String contentType = Optional.ofNullable(file.getContentType())
                    .orElseGet(() -> S3Util.getContentType(objectName));
            return putObject(bucketName, objectName, inputStream, file.getSize(), contentType);
        }
    }

    /**
     * 上传本地文件
     */
    @SneakyThrows
    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, File file) {
        // 使用 NIO 的 Files.newInputStream
        Path filePath = file.toPath();
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            String contentType = S3Util.getContentType(file.getName());
            return putObject(bucketName, objectName, inputStream, file.length(), contentType);
        }
    }

    /**
     * 上传字节数组
     */
    @SneakyThrows
    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, byte[] data, String contentType) {
        try (ByteArrayInputStream stream = new ByteArrayInputStream(data)) {
            return putObject(bucketName, objectName, stream, data.length, contentType);
        }
    }

    /**
     * 通过bucketName和objectName获取对象
     */
    @SneakyThrows
    @Override
    public GetObjectResponse getObject(String bucketName, String objectName) {
        try {
            ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(
                    GetObjectRequest.builder().bucket(bucketName).key(objectName).build());
            return stream.response();
        } catch (NoSuchKeyException e) {
            log.error("对象不存在: {}/{}", bucketName, objectName);
            throw new RuntimeException("对象不存在: " + objectName, e);
        } catch (S3Exception e) {
            log.error("获取对象失败: {}/{}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 下载对象到本地文件
     */
    @SneakyThrows
    @Override
    public void downloadObject(String bucketName, String objectName, Path targetPath) {
        try (ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(
                GetObjectRequest.builder().bucket(bucketName).key(objectName).build())) {
            // 使用 Java 标准库
            Files.copy(stream, targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("文件下载成功: {} -> {}", objectName, targetPath);
        } catch (S3Exception e) {
            log.error("下载对象失败: {}/{}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 获取对象的字节数据
     */
    @SneakyThrows
    @Override
    public byte[] getObjectBytes(String bucketName, String objectName) {
        try (ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(
                GetObjectRequest.builder().bucket(bucketName).key(objectName).build())) {

            return IoUtils.toByteArray(stream);
        } catch (S3Exception e) {
            log.error("获取对象字节数据失败: {}/{}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 获取对象的url
     */
    @SneakyThrows
    @Override
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(expires))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();
            log.debug("生成预签名URL: {}", url);
            return url;
        } catch (S3Exception e) {
            log.error("生成预签名URL失败: {}/{}", bucketName, objectName, e);
            throw new RuntimeException("生成预签名URL失败", e);
        }
    }

    /**
     * 获取对象的永久URL（如果Bucket是公共读）
     */
    @SneakyThrows
    @Override
    public String getObjectURL(String bucketName, String objectName) {
        try {
            URL url = s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build());
            return url.toString();
        } catch (S3Exception e) {
            log.error("获取对象URL失败: {}/{}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 通过bucketName和objectName删除对象
     */
    @SneakyThrows
    @Override
    public void removeObject(String bucketName, String objectName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build());
            log.info("对象删除成功: {}/{}", bucketName, objectName);
        } catch (S3Exception e) {
            log.error("删除对象失败: {}/{}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 批量删除对象
     */
    @SneakyThrows
    @Override
    public void removeObjects(String bucketName, List<String> objectNames) {
        if (objectNames == null || objectNames.isEmpty()) {
            return;
        }

        try {
            List<ObjectIdentifier> objectsToDelete = objectNames.stream()
                    .map(key -> ObjectIdentifier.builder().key(key).build())
                    .collect(Collectors.toList());

            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(Delete.builder().objects(objectsToDelete).build())
                    .build();

            s3Client.deleteObjects(deleteRequest);
            log.info("批量删除对象成功: {}，共{}个对象", bucketName, objectNames.size());
        } catch (S3Exception e) {
            log.error("批量删除对象失败: {}", bucketName, e);
            throw e;
        }
    }

    /**
     * 根据bucketName和prefix获取对象集合
     */
    @SneakyThrows
    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response response = s3Client.listObjectsV2(request);
            return response.contents().stream()
                    .map(s3Object -> S3ObjectSummary.builder()
                            .bucketName(bucketName)
                            .objectName(s3Object.key())
                            .size(s3Object.size())
                            .lastModified(s3Object.lastModified())
                            .storageClass(s3Object.storageClass().toString())
                            .build())
                    .collect(Collectors.toList());
        } catch (S3Exception e) {
            log.error("根据前缀获取对象列表失败: {}/{}", bucketName, prefix, e);
            throw e;
        }
    }

    /**
     * 检查对象是否存在
     */
    @SneakyThrows
    @Override
    public boolean objectExists(String bucketName, String objectName) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            log.error("检查对象存在性失败: {}/{}", bucketName, objectName, e);
            return false;
        }
    }

    /**
     * 复制对象
     */
    @SneakyThrows
    @Override
    public CopyObjectResponse copyObject(String sourceBucket, String sourceKey,
                                         String destinationBucket, String destinationKey) {
        try {
            CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                    .sourceBucket(sourceBucket)
                    .sourceKey(sourceKey)
                    .destinationBucket(destinationBucket)
                    .destinationKey(destinationKey)
                    .build();

            return s3Client.copyObject(copyRequest);
        } catch (S3Exception e) {
            log.error("复制对象失败: {}/{} -> {}/{}", sourceBucket, sourceKey, destinationBucket, destinationKey, e);
            throw e;
        }
    }

    /**
     * 核心上传方法
     */
    @SneakyThrows
    private PutObjectResponse putObject(String bucketName, String objectName, InputStream stream,
                                        long size, String contextType) {
        // 确保Bucket存在
        if (bucketNotExists(bucketName)) {
            createBucket(bucketName);
        }

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .contentType(contextType)
                    .contentLength(size)
                    .build();

            // 对于小文件，可以直接读取到内存
            if (size <= 10 * 1024 * 1024) { // 10MB以下
//                byte[] bytes = IoUtils.toByteArray(stream);  // 关键区别：先读取到内存
//                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
//                    RequestBody requestBody = RequestBody.fromInputStream(byteArrayInputStream, bytes.length);
//                    PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
//                    log.info("文件上传成功: {}/{}，大小: {} bytes", bucketName, objectName, size);
//                    return response;
//                }

                // 小文件：直接使用字节数组
                byte[] bytes = IoUtils.toByteArray(stream);
                RequestBody requestBody = RequestBody.fromBytes(bytes);
                PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
                log.info("小文件上传成功: {}/{}，大小: {} bytes", bucketName, objectName, size);
                return response;
            } else {
                // 大文件使用流式上传
                RequestBody requestBody = RequestBody.fromInputStream(stream, size);
                PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
                log.info("大文件上传成功: {}/{}，大小: {} bytes", bucketName, objectName, size);
                return response;
            }
        } catch (S3Exception e) {
            log.error("文件上传失败: {}/{}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 设置 Bucket 策略
     */
    @SneakyThrows
    @Override
    public void setBucketPolicy(String bucketName, String policy) {
        try {
            PutBucketPolicyRequest policyReq = PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(policy)
                    .build();
            s3Client.putBucketPolicy(policyReq);
            log.info("Bucket策略设置成功: {}", bucketName);
        } catch (S3Exception e) {
            log.error("设置Bucket策略失败: {}", bucketName, e);
            throw e;
        }
    }

    /**
     * 获取 Bucket 策略
     */
    @SneakyThrows
    @Override
    public String getBucketPolicy(String bucketName) {
        try {
            GetBucketPolicyRequest request = GetBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .build();

            GetBucketPolicyResponse response = s3Client.getBucketPolicy(request);
            return response.policy();
        } catch (Exception e) {
            log.error("获取Bucket策略失败: {}", bucketName, e);
            throw e;
        }
    }

    /**
     * 删除 Bucket 策略
     */
    @SneakyThrows
    @Override
    public void deleteBucketPolicy(String bucketName) {
        try {
            DeleteBucketPolicyRequest request = DeleteBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .build();

            s3Client.deleteBucketPolicy(request);
            log.info("Bucket策略删除成功: {}", bucketName);
        } catch (S3Exception e) {
            log.error("删除Bucket策略失败: {}", bucketName, e);
            throw e;
        }
    }
}
