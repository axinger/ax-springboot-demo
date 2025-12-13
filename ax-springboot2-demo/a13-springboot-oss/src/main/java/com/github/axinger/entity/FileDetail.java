package com.github.axinger.entity;

import lombok.Data;

@Data
public class FileDetail {

    private String id;
    private String url;

    private String metadata;
    private String userMetadata;
    private String thMetadata;
    private String thUserMetadata;
    private String attr;
    private String hashInfo;
}
