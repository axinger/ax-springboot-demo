package com.github.axinger.oos.service;

import com.github.axinger.oos.dto.S3ObjectSummary;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.CopyObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface S3Template {
    @SneakyThrows
    void createBucket(String bucketName);

    @SneakyThrows
    List<Bucket> getAllBuckets();

    @SneakyThrows
    void removeBucket(String bucketName);

    @SneakyThrows
    PutObjectResponse putObject(String bucketName, String objectName, InputStream stream, String contextType);

    @SneakyThrows
    PutObjectResponse putObject(String bucketName, String objectName, InputStream stream);

    @SneakyThrows
    PutObjectResponse putObject(String bucketName, String objectName, MultipartFile file);

    @SneakyThrows
    PutObjectResponse putObject(String bucketName, String objectName, File file);

    @SneakyThrows
    PutObjectResponse putObject(String bucketName, String objectName, byte[] data, String contentType);

    @SneakyThrows
    GetObjectResponse getObject(String bucketName, String objectName);

    @SneakyThrows
    void downloadObject(String bucketName, String objectName, Path targetPath);

    @SneakyThrows
    byte[] getObjectBytes(String bucketName, String objectName);

    @SneakyThrows
    String getObjectURL(String bucketName, String objectName, Integer expires);

    @SneakyThrows
    String getObjectURL(String bucketName, String objectName);

    @SneakyThrows
    void removeObject(String bucketName, String objectName);

    @SneakyThrows
    void removeObjects(String bucketName, List<String> objectNames);

    @SneakyThrows
    List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);

    @SneakyThrows
    boolean objectExists(String bucketName, String objectName);

    @SneakyThrows
    CopyObjectResponse copyObject(String sourceBucket, String sourceKey,
                                  String destinationBucket, String destinationKey);

    @SneakyThrows
    void setBucketPolicy(String bucketName, String policy);

    @SneakyThrows
    String getBucketPolicy(String bucketName);

    @SneakyThrows
    void deleteBucketPolicy(String bucketName);
}
