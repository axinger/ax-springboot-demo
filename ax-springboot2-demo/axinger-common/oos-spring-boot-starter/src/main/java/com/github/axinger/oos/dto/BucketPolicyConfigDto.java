package com.github.axinger.oos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Bucket访问策略配置
 */
@Data
@EqualsAndHashCode
@Builder
public class BucketPolicyConfigDto {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Statement")
    private List<Statement> statement;

    @Data
    @EqualsAndHashCode
    @Builder
    public static class Statement {
        @JsonProperty("Effect")
        private String effect;
        @JsonProperty("Principal")
        private Principal principal;
        @JsonProperty("Action")
        private String[] action;
        @JsonProperty("Resource")
        private String[] resource;

    }

    @Data
    @EqualsAndHashCode
    @Builder
    public static class Principal {
        @JsonProperty("AWS")
        private String[] aws;
    }
}
