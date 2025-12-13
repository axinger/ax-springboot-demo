package com.github.axinger.oos.config;

import com.github.axinger.oos.bean.OssProperties;
import com.github.axinger.oos.service.OssTemplate;
import com.github.axinger.oos.service.S3Template;
import com.github.axinger.oos.service.impl.OssTemplateImpl;
import com.github.axinger.oos.service.impl.S3TemplateImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OssProperties.class)
public class OssAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public S3Client s3Client(OssProperties ossProperties) {

        // 构建 S3 配置
//        S3Configuration s3Configuration = S3Configuration.builder()
//                .pathStyleAccessEnabled(ossProperties.getPathStyleAccess())  // 路径样式访问
//                .build();

        // 构建 HTTP 客户端配置

//        SdkHttpClient.builder()
//                .maxConnections(maxConnections)  // 最大连接数
//                .connectionTimeout(Duration.ofMillis(connectionTimeout))  // 连接超时
//                .socketTimeout(Duration.ofMillis(socketTimeout));  // Socket 超时

        // 初始化 S3 客户端
        return S3Client.builder()
                .endpointOverride(URI.create(ossProperties.getEndpoint())) // RustFS 地址
                .region(Region.of(ossProperties.getRegion())) // 可写死，RustFS 不校验 region
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ossProperties.getAccessKeyId(), ossProperties.getSecretAccessKey())))
                .forcePathStyle(ossProperties.getPathStyleAccess()) // 关键配置！RustFS 需启用 Path-Style
//                .serviceConfiguration(s3Configuration)
//                .httpClientBuilder(httpClientBuilder)
                .build();
    }


    @Bean
    @ConditionalOnMissingBean
    public S3Presigner s3Presigner(OssProperties ossProperties) {
        return S3Presigner.builder()
                .region(Region.of(ossProperties.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ossProperties.getAccessKeyId(), ossProperties.getSecretAccessKey())))
                .build();
    }

    @Bean
    @ConditionalOnBean(value = {S3Client.class, S3Presigner.class})
    @ConditionalOnMissingBean(OssTemplate.class)
    public OssTemplate ossTemplate(S3Client s3Client, S3Presigner s3Presigner) {
        return new OssTemplateImpl(s3Client, s3Presigner);
    }


    @Bean
    @ConditionalOnBean(value = {S3Client.class, S3Presigner.class})
    @ConditionalOnMissingBean(S3Template.class)
    public S3Template s3Template(S3Client s3Client, S3Presigner s3Presigner) {
        return new S3TemplateImpl(s3Client, s3Presigner);
    }
}
