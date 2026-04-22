package com.axing.common.minio.config;

import com.axing.common.minio.bean.MinioProperties;
import com.axing.common.minio.service.MinioTemplate;
import com.axing.common.minio.service.impl.MinioTemplateImpl;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;


/**
 * 配置类
 *
 * @author luoyu
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({MinioProperties.class})
public class MinioAutoConfig {

    public static String MINIO_HOST;
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient(MinioProperties minioProperties) {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getUsername(), minioProperties.getPassword())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(MinioTemplate.class)
    public MinioTemplate minioTemplate(MinioClient minioClient) {
        return new MinioTemplateImpl(minioClient);
    }


    @PostConstruct
    void init() {
        MINIO_HOST = minioProperties.getUrl();
    }
}
