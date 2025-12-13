package com.axing.common.minio.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.axing.common.minio.error.MinioException;
import com.axing.common.minio.model.UploadFileBO;
import com.axing.common.minio.service.MinioService;
import com.axing.common.minio.util.FilePathUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    @SneakyThrows
    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (MinioException e) {
            log.error("检查存储桶是否存在失败 = {}", e.getMessage());
            throw new MinioException(StrUtil.format("检查存储桶是否存在失败 = {}", e.getMessage()));
        }
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return boolean 成功
     */
    @SneakyThrows
    @Override
    public boolean createBucket(String bucketName) {
        try {
            if (!this.bucketExists(bucketName)) {
                MakeBucketArgs args = MakeBucketArgs.builder().bucket(bucketName).build();
                minioClient.makeBucket(args);
                log.info("创建存储桶成功");
            }
            return true;
        } catch (MinioException e) {
            log.error("创建存储桶失败 = {}", e.getMessage());
            throw new MinioException(StrUtil.format("创建存储桶失败 = {}", e.getMessage()));
        }
    }

    /**
     * 根据存储桶名称获取信息
     *
     * @param bucketName 存储桶名称
     * @return 桶信息
     */
    @SneakyThrows
    @Override
    public Bucket getBucket(String bucketName) {
        try {
            return minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst().orElse(null);
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("根据存储桶名称获取信息失败 = {}", e.getMessage()));
        }
    }

    /**
     * 根据存储桶删除信息
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    @Override
    public boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return true;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("根据存储桶删除信息失败 = {}", e.getMessage()));
        }
    }

    /**
     * 查看文件对象
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return Item 列表
     */
    @SneakyThrows
    @Override
    public List<Item> getItemsByPrefix(String bucketName, String prefix, Boolean recursive) {
        try {
            List<Item> objectList = new ArrayList<>();

            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build();
            Iterable<Result<Item>> objectsIterator = minioClient.listObjects(args);
            for (Result<Item> result : objectsIterator) {
                objectList.add(result.get());
            }
            return objectList;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("根据文件前缀查询文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 获取文件流
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    @SneakyThrows
//    @Override
    public InputStream getObject(String bucketName, String objectName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("获取文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 获取全部存储桶
     *
     * @return List<Bucket>
     */
    @SneakyThrows
    @Override
    public List<Bucket> getBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("获取全部存储桶失败 = {}", e.getMessage()));
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream inputStream
     * @param objectName  objectName
     * @param bucketName  bucketName
     * @param contentType contentType
     */
    @SneakyThrows
    @Override
    public UploadFileBO uploadStream(InputStream inputStream, String bucketName, String objectName, String contentType) {
        try {
            // 检查存储桶是否已经存在，不存在则创建
            this.createBucket(bucketName);
            // 使用putObject上传一个文件到存储桶中。
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    // 文件流，文件大小，分片大小（通常为 -1，自动推断，若手动填写，必须在 5M -5G 之间）
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build();
            ObjectWriteResponse response = minioClient.putObject(args);
            Assert.notNull(response, "上传文件失败,response返回结果null");

//            minioClient.uploadObject(UploadObjectArgs.builder()
//                    .bucket(bucketName)
//                    .object(objectName)
//                    .contentType(contentType)
//                    .filename("C:\\Users\\j9967\\Downloads\\lifecycle-events.png")
//                    .build());

            // 关闭
//            inputStream.close();
            UploadFileBO bo = new UploadFileBO();
            bo.setBucket(response.bucket());
            bo.setObject(response.object());
            log.info("上传文件成功 = {}", bo);
            return bo;
        } catch (MinioException e) {
            log.error("uploadStream失败 = {}", e.getMessage());
            throw new MinioException(StrUtil.format("上传文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 上传MultipartFile
     *
     * @param file       文件
     * @param bucketName 桶
     * @return 文件
     */
    @SneakyThrows
    @Override
    public UploadFileBO uploadFile(MultipartFile file, String bucketName) {
        Assert.isTrue(file.getSize() > 0, "上传文件失败,文件大小不能为：0");
        try (InputStream inputStream = file.getInputStream()) {
            String originalFilename = file.getOriginalFilename();
            Assert.notNull(originalFilename, "上传文件失败,文件名为空");
            String contentType = file.getContentType();
            Assert.notNull(inputStream, "上传文件失败,getInputStream为空");
            String objectName = FilePathUtil.getFileName(originalFilename);
            UploadFileBO bo = uploadStream(inputStream, bucketName, objectName, contentType);
            bo.setOriginalFilename(originalFilename);
            bo.setSize(file.getSize());
            bo.setContentType(contentType);
            return bo;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("上传MultipartFile失败 = {}", e.getMessage()));
        }
    }

    /**
     * 下载文件
     *
     * @param response   response
     * @param objectName objectName
     */
    @SneakyThrows
    @Override
    public void download(HttpServletResponse response, String bucketName, String objectName) {
        try (InputStream inputStream = this.getObject(bucketName, objectName)) {
            StatObjectArgs args = StatObjectArgs.builder().bucket(bucketName).object(objectName).build();
            StatObjectResponse stat = minioClient.statObject(args);
            response.setContentType(stat.contentType());
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectName, String.valueOf(StandardCharsets.UTF_8)));
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("下载文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 流式下载
     *
     * @param bucket   bucketName
     * @param fileName objectName
     */
    @Override
    public ResponseEntity<StreamingResponseBody> downloadStreaming(String bucket, String fileName) {

        try {
            // 从 MinIO 获取文件流
            InputStream inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build());

            // 使用 StreamingResponseBody 返回流
            StreamingResponseBody responseBody = outputStream -> {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close(); // 关闭输入流
            };

            // 设置文件下载响应头
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

//            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
            return ResponseEntity.ok().headers(headers).body(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 写入文件到本地路径
     *
     * @param bucketName   桶
     * @param objectName   文件名
     * @param fullFilePath 指定路径
     */
    @SneakyThrows
    @Override
    public File writeToPath(String bucketName, String objectName, String fullFilePath) {
        try (InputStream inputStream = this.getObject(bucketName, objectName)) {
            return FileUtil.writeFromStream(inputStream, fullFilePath);
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("写入文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 复制文件到指定位置
     *
     * @param sourceBucket 源桶
     * @param source       源文件
     * @param targetBucket 目标桶
     * @param target       目标文件
     * @return 目标文件
     */
    @SneakyThrows
    @Override
    public String copyObject(String sourceBucket, String source, String targetBucket, String target) {
        try {
            ObjectWriteResponse response = minioClient.copyObject(CopyObjectArgs.builder().bucket(sourceBucket).object(target).source(CopySource.builder().bucket(targetBucket).object(source).build()).build());
            log.info("minio复制文件成功 source = {},target = {}", source, target);
            return response.object();
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("minio复制文件失败 = {}", e.getMessage()));
        }
    }


    /**
     * 获取所有文件
     *
     * @param bucketName bucketName
     */
    @SneakyThrows
    @Override
    public List<Item> list(String bucketName) {
        try {
            List<Item> list = new ArrayList<>();
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
            for (Result<Item> result : results) {
                list.add(result.get());
            }
            return list;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("获取所有文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 文件名
     */
    @SneakyThrows
    @Override
    public boolean deleteObjectName(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("删除文件失败 = {}", e.getMessage()));
        }
    }

    /**
     * 批量删除文件
     *
     * @param bucketName  bucketName
     * @param objectNames 文件名列表
     */
    @Override
    public void deleteObjectNames(String bucketName, List<String> objectNames) {
        objectNames.forEach(objectNamesItem -> {
            this.deleteObjectName(bucketName, objectNamesItem);
        });
    }

    /**
     * 创建上传文件对象的外链
     *
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @param expiry     过期时间(秒) 最大为7天 超过7天则默认最大值
     * @return uploadUrl
     */
    @SneakyThrows
    @Override
    public String createUploadUrl(String bucketName, String objectName, Integer expiry) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().method(Method.PUT).bucket(bucketName).object(objectName).expiry(expiry).build());
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("创建上传文件对象的外链失败 = {}", e.getMessage()));
        }
    }


    /**
     * 获取文件url
     *
     * @param objectName objectName
     *                   过期时间(秒) 最大为7天 超过7天则默认最大值
     * @return url
     */
    @SneakyThrows
    @Override
    public String fileUrl(String bucketName, String objectName) {
        try {
            new GetPresignedObjectUrlArgs();
            GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder().method(Method.GET).bucket(bucketName).object(objectName).expiry(7, TimeUnit.DAYS).build();
            return minioClient.getPresignedObjectUrl(build);
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("获取文件url失败 = {}", e.getMessage()));
        }
    }

    /**
     * 批量创建分片上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param chunkCount 分片数量
     * @param expiry     过期时间(秒) 最大为7天 超过7天则默认最大值
     * @return uploadChunkUrls
     */
    @Override
    public List<String> createUploadChunkUrlList(String bucketName, String objectMD5, Integer chunkCount, Integer expiry) {
        objectMD5 += "/";
        if (null == chunkCount || 0 == chunkCount) {
            return null;
        }
        List<String> urlList = new ArrayList<>(chunkCount);
        for (int i = 1; i <= chunkCount; i++) {
            String objectName = objectMD5 + i + ".chunk";
            urlList.add(this.createUploadUrl(bucketName, objectName, expiry));
        }
        return urlList;
    }

    /**
     * 创建指定序号的分片文件上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param partNumber 分片序号
     * @param expiry     过期时间(秒) 最大为7天 超过7天则默认最大值
     * @return uploadChunkUrl
     */
    @Override
    public String createUploadChunkUrl(String bucketName, String objectMD5, Integer partNumber, Integer expiry) {
        objectMD5 += "/" + partNumber + ".chunk";
        return this.createUploadUrl(bucketName, objectMD5, expiry);
    }

    /**
     * 获取分片文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称前缀（ObjectMd5）
     * @param sort       是否排序(升序)
     * @return objectNames
     */
    @SneakyThrows
    @Override
    public List<String> listObjectNames(String bucketName, String prefix, Boolean sort) {
        try {
            ListObjectsArgs listObjectsArgs;
            if (null == prefix) {
                listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).recursive(true).build();
            } else {
                listObjectsArgs = ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(true).build();
            }
            Iterable<Result<Item>> chunks = minioClient.listObjects(listObjectsArgs);
            List<String> chunkPaths = new ArrayList<>();
            for (Result<Item> item : chunks) {
                chunkPaths.add(item.get().objectName());
            }
            if (sort) {
                return chunkPaths.stream().distinct().collect(Collectors.toList());
            }
            return chunkPaths;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("获取分片文件名称列表失败 = {}", e.getMessage()));
        }
    }

    /**
     * 获取分片名称地址，HashMap：key=分片序号，value=分片文件地址
     *
     * @param bucketName 存储桶名称
     * @param ObjectMd5  对象Md5
     * @return objectChunkNameMap
     */
    @Override
    public Map<Integer, String> mapChunkObjectNames(String bucketName, String ObjectMd5, Boolean sort) {
        List<String> chunkPaths = this.listObjectNames(bucketName, ObjectMd5, sort);
        if (CollectionUtils.isEmpty(chunkPaths)) {
            return null;
        }
        Map<Integer, String> chunkMap = new HashMap<>(chunkPaths.size());
        for (String chunkName : chunkPaths) {
            Integer partNumber = Integer.parseInt(chunkName.substring(chunkName.indexOf("/") + 1, chunkName.lastIndexOf(".")));
            chunkMap.put(partNumber, chunkName);
        }
        return chunkMap;
    }

    /**
     * 合并分片文件成对象文件
     *
     * @param chunkBucKetName   分片文件所在存储桶名称
     * @param composeBucketName 合并后的对象文件存储的存储桶名称
     * @param chunkNames        分片文件名称集合
     * @param objectName        合并后的对象文件名称
     * @return true/false
     */
    @SneakyThrows
    @Override
    public boolean composeObject(String chunkBucKetName, String composeBucketName, List<String> chunkNames, String objectName) {
        try {
            List<ComposeSource> sourceObjectList = new ArrayList<>(chunkNames.size());
            for (String chunk : chunkNames) {
                sourceObjectList.add(ComposeSource.builder().bucket(chunkBucKetName).object(chunk).build());
            }
            minioClient.composeObject(ComposeObjectArgs.builder().bucket(composeBucketName).object(objectName).sources(sourceObjectList).build());
            return true;
        } catch (MinioException e) {
            throw new MinioException(StrUtil.format("合并分片文件成对象文件失败 = {}", e.getMessage()));
        }
    }

}
