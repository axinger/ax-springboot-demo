package com.github.axinger.auth.db.dto;


import lombok.Data;

@Data
public class ApiPermissionDTO {
    private String path;
    private String method;
    private String permissionCode;
}
