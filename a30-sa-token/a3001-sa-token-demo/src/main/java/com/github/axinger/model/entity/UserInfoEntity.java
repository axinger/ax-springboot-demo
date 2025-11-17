package com.github.axinger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoEntity {
    private String userId;
    private String username;
    private String password;
    private OrganizationEntity org;

    private List<PermissionEntity> permission;
    private List<RoleEntity> role;
}
