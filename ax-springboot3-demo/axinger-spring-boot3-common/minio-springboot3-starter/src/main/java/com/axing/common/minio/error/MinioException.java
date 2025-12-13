package com.axing.common.minio.error;

public class MinioException extends RuntimeException {
    public MinioException(String message) {
        super(message);
    }
}
