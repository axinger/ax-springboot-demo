package com.axing.common.minio.model;

import cn.hutool.core.util.StrUtil;
import com.axing.common.minio.config.MinioAutoConfig;
import lombok.Data;

@Data
public class UploadFileBO {

    /**
     * 桶名
     */
    private String bucket;

    /**
     * 文件路径/文件名
     */
    private String object;

    /**
     * 文件名
     */
    private String originalFilename;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件类型
     */
    private String contentType;
    /**
     * 全路径
     */
    private String fullPath = getFullPath();

    public String getFullPath() {
        return StrUtil.format("{}/{}/{}", MinioAutoConfig.MINIO_HOST, bucket, object);
    }

}
