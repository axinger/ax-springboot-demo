package com.github.axinger.oos.service.impl;

import com.github.axinger.oos.dto.S3ObjectSummary;
import com.github.axinger.oos.exception.OssException;
import com.github.axinger.oos.service.OssTemplate;
import com.github.axinger.oos.util.S3Util;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.utils.IoUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public record OssTemplateImpl(S3Client s3Client, S3Presigner s3Presigner) implements OssTemplate {

    // 构造检查
    public OssTemplateImpl {
        Objects.requireNonNull(s3Client, "S3Client must not be null");
        Objects.requireNonNull(s3Presigner, "S3Presigner must not be null");
    }

    /**
     * 判断 Bucket 是否存在
     */
    private boolean bucketExists(String bucketName) {
        try {
            s3Client.headBucket(r -> r.bucket(bucketName));
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        } catch (S3Exception e) {
            log.warn("Error checking bucket existence: {}", bucketName, e);
            throw new OssException("Failed to check bucket: " + bucketName, e);
        }
    }

    /**
     * 创建 Bucket（线程安全）
     */
    @Override
    public void createBucket(String bucketName) {
        if (bucketExists(bucketName)) {
            log.info("Bucket already exists: {}", bucketName);
            return;
        }

        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            log.info("Bucket created: {}", bucketName);

            // 设置只读策略
            String policy = S3Util.createBucketPolicyConfigDto(bucketName);
            s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                    .bucket(bucketName)
                    .policy(policy)
                    .build());
            log.info("Bucket policy set: {}", bucketName);
        } catch (S3Exception e) {
            if ("BucketAlreadyOwnedByYou".equals(e.awsErrorDetails().errorCode())) {
                log.info("Bucket was just created by another thread: {}", bucketName);
            } else {
                log.error("Failed to create bucket: {}", bucketName, e);
                throw new OssException("Failed to create bucket: " + bucketName, e);
            }
        }
    }

    @Override
    public List<Bucket> getAllBuckets() {
        try {
            return s3Client.listBuckets().buckets();
        } catch (SdkClientException e) {
            log.error("Failed to list buckets", e);
            throw new OssException("Failed to list buckets", e);
        }
    }

    @Override
    public void removeBucket(String bucketName) {
        try {
            s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
            log.info("Bucket deleted: {}", bucketName);
        } catch (S3Exception e) {
            log.error("Failed to delete bucket: {}", bucketName, e);
            throw new OssException("Failed to delete bucket: " + bucketName, e);
        }
    }

    // --- 上传对象 ---

    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream, String contentType) {
        try {
            byte[] bytes = IoUtils.toByteArray(stream);
            return doPutObject(bucketName, objectName, new ByteArrayInputStream(bytes), bytes.length, contentType);
        } catch (IOException e) {
            throw new OssException("Failed to read input stream", e);
        }
    }

    @Override
    public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream) {
        return putObject(bucketName, objectName, stream, "application/octet-stream");
    }

    private PutObjectResponse doPutObject(String bucketName, String objectName, InputStream stream, long size, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .contentType(contentType)
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(stream, size);
            PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);

            log.info("Object uploaded: bucket={}, object={}, size={} bytes, contentType={}",
                    bucketName, objectName, size, contentType);

            return response;
        } catch (SdkClientException e) {
            log.error("Upload failed: bucket={}, object={}", bucketName, objectName, e);
            throw new OssException("Upload failed: " + bucketName + "/" + objectName, e);
        }
    }

    // --- 下载对象 ---

    @Override
    public ResponseInputStream<GetObjectResponse> getObject(String bucketName, String objectName) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();
            ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(request);
            log.info("Object downloaded: {}/{}", bucketName, objectName);
            return stream;
        } catch (S3Exception e) {
            log.error("Get object failed: {}/{}", bucketName, objectName, e);
            throw new OssException("Get object failed: " + bucketName + "/" + objectName, e);
        }
    }

    // --- 生成预签名 URL（推荐方式）---

    @Override
    public String getObjectURL(String bucketName, String objectName, Integer expiresMinutes) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(expiresMinutes))
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            log.debug("Generated presigned URL: {} (expires in {} min)", url, expiresMinutes);
            return url;
        } catch (Exception e) {
            throw new OssException("Failed to generate presigned URL for " + bucketName + "/" + objectName, e);
        }
    }

    // --- 删除对象 ---

    @Override
    public void removeObject(String bucketName, String objectName) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectName)
                    .build());
            log.info("Object deleted: {}/{}", bucketName, objectName);
        } catch (S3Exception e) {
            log.error("Delete object failed: {}/{}", bucketName, objectName, e);
            throw new OssException("Delete object failed: " + bucketName + "/" + objectName, e);
        }
    }

    // --- 列出所有对象（支持分页）---

    @Override
    public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix);

        if (!recursive) {
            requestBuilder.delimiter("/"); // 模拟目录结构
        }

        List<S3ObjectSummary> result = s3Client.listObjectsV2Paginator(requestBuilder.build())
                .contents()
                .stream()
                .map(s3Object -> S3ObjectSummary.builder()
                        .bucketName(bucketName)
                        .objectName(s3Object.key())
                        .size(s3Object.size())
                        .lastModified(s3Object.lastModified())
                        .storageClass(s3Object.storageClass().toString())
                        .build()
                )
                .collect(Collectors.toList());

        log.debug("Listed {} objects under bucket='{}', prefix='{}', recursive={}",
                result.size(), bucketName, prefix, recursive);

        return result;
    }
}
