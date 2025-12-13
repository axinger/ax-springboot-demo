package com.github.axinger.oos.util;

import cn.hutool.core.io.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.axinger.oos.dto.BucketPolicyConfigDto;

import java.util.List;
import java.util.Objects;

public class S3Util {


    /**
     * 使用 Jackson 构建 Policy（类型安全）
     */
    public static String createBucketPolicyWithJackson(String bucketName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode policy = mapper.createObjectNode();
            policy.put("Version", "2012-10-17");

            ArrayNode statements = mapper.createArrayNode();
            ObjectNode statement = mapper.createObjectNode();
            statement.put("Effect", "Allow");
            statement.set("Principal", mapper.createObjectNode().put("AWS", "*"));
            statement.set("Action", mapper.createArrayNode().add("s3:GetObject"));
            statement.set("Resource", mapper.createArrayNode()
                    .add("arn:aws:s3:::" + bucketName + "/*"));

            statements.add(statement);
            policy.set("Statement", statements);

            return mapper.writeValueAsString(policy);
        } catch (Exception e) {
            throw new RuntimeException("Policy 构建失败", e);
        }
    }

    /**
     * 创建 Bucket 的只读访问策略
     */
    public static String createBucketPolicyConfigDto(String bucketName) {
        try {
            BucketPolicyConfigDto.Statement statement = BucketPolicyConfigDto.Statement.builder()
                    .effect("Allow")
                    .principal(BucketPolicyConfigDto.Principal.builder().aws(new String[]{"*"}).build())
                    .action(new String[]{"s3:GetObject"})
                    .resource(new String[]{"arn:aws:s3:::" + bucketName + "/*"})
                    .build();
            BucketPolicyConfigDto dto = BucketPolicyConfigDto.builder()
                    .version("2012-10-17")
                    .statement(List.of(statement))
                    .build();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(dto);
        } catch (Exception e) {
            throw new RuntimeException("Policy 构建失败", e);
        }
    }


    /**
     * 根据文件名获取Content-Type
     */
    public static String getContentType(String fileName) {
        if (Objects.isNull(fileName)) {
            return "application/octet-stream";
        }

        String extension = FileUtil.getSuffix(fileName).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "webp" -> "image/webp";
            case "pdf" -> "application/pdf";
            case "txt" -> "text/plain";
            case "html", "htm" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "zip" -> "application/zip";
            case "rar" -> "application/x-rar-compressed";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls" -> "application/vnd.ms-excel";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt" -> "application/vnd.ms-powerpoint";
            case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "mp4" -> "video/mp4";
            case "mp3" -> "audio/mpeg";
            default -> "application/octet-stream";
        };
    }
}
