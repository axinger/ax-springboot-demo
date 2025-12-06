package com.github.axinger.auth.db.dto;

import lombok.Data;

@Data
public class UserPermissionsDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private String category;
}
