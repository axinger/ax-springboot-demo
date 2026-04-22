package com.github.axinger.oos.dto;

import java.time.Instant;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Builder
public class S3ObjectSummary {

    private String bucketName;

    private String objectName;

    private long size;

    private Instant lastModified;

    private String storageClass;

}
