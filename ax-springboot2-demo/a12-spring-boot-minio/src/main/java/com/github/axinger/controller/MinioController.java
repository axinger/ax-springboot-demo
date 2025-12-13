package com.github.axinger.controller;

import com.axing.common.minio.service.MinioTemplate;
import com.axing.common.minio.util.FilePathUtil;
import com.axing.common.response.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * minio 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Slf4j
@Tag(name = "MinioController")
@RestController
public class MinioController {

    String DOWNLOAD_URL = "https://minio.com/";
    @Autowired
    private MinioTemplate minioTemplate;
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 上传文件,minio用 InputStream 方式
     */
    @Operation(summary = "上传文件,minio用 InputStream 方式")
    @PostMapping(value = "/minio/uploadStream", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadByMinio(@RequestParam(value = "file") MultipartFile file,
                                   @RequestParam("bucketName") String bucketName) throws Exception {
        if (file.getSize() < 1) {
            log.warn("文件大小为：0");
            return Result.fail("文件大小为：0");
        }
        String fileName = file.getOriginalFilename();
        @Cleanup InputStream inputStream = file.getInputStream();
        String contentType = file.getContentType();
        String patchName = FilePathUtil.getFileName(fileName);
        Object upload = minioTemplate.uploadStream(inputStream, bucketName, patchName, contentType);
        return Result.success(upload);
    }

    /**
     * 上传文件,用 MultipartFile 方式,内部也是走InputStream方式
     */
    @PostMapping(value = "/minio/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam("bucketName") String bucketName) {
        Object upload = minioTemplate.uploadFile(file, bucketName);
        return Result.success(upload);
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{bucket}/{fileName}")
    public void downloadByMinio(HttpServletResponse response, @PathVariable String bucket,
                                @PathVariable String fileName) {
        minioTemplate.download(response, bucket, fileName);
    }

    /**
     * 流失下载文件
     */
    @GetMapping("/downloadStreaming/{bucket}/{fileName}")
    public ResponseEntity<StreamingResponseBody> downloadStreaming(@PathVariable String bucket,
                                                                   @PathVariable String fileName) {
        return minioTemplate.downloadStreaming(bucket, fileName);
    }

    /**
     * 获取文件url
     *
     * @return url
     */
    @GetMapping("/minio/fileUrl")
    public Result<?> downloadByMinio(String bucketName, String fileName) {
        String url = minioTemplate.fileUrl(bucketName, fileName);
        return Result.success(url);
    }

    /**
     * 下载文件到本地
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    @GetMapping("/minio/writeToPath/path")
    public Result<?> downloadByMinio(String bucketName, String fileName, String path) {
        return Result.success(minioTemplate.writeToPath(bucketName, fileName, path));
    }

    @GetMapping("/v1/file/download3/{fileId}")
    public void download3(@PathVariable("fileId") String fileId, HttpServletResponse response) {
        log.info("download file:{}", fileId);
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("fileId", fileId);
        ResponseExtractor<Boolean> responseExtractor = clientHttpResponse -> {
            // 设置响应头，直接用第三方文件服务的响应头
            HttpHeaders headers = clientHttpResponse.getHeaders();
            headers.forEach((key, value) -> response.setHeader(key, value.get(0)));
            // 收到响应输入流即时拷贝写出到响应输出流中: inputStream -> outputStream
            StreamUtils.copy(clientHttpResponse.getBody(), response.getOutputStream());
            return true;
        };
        Boolean execute = restTemplate.execute(DOWNLOAD_URL, HttpMethod.GET, null, responseExtractor, uriVariables);
        log.info("download file success?{}", execute);
    }
}
