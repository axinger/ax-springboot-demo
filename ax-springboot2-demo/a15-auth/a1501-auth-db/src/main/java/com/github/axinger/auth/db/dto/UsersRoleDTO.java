package com.github.axinger.auth.db.dto;

import lombok.Data;

@Data
public class UsersRoleDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
}
