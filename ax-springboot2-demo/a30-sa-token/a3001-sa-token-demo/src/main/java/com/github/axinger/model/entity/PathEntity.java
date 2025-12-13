package com.github.axinger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PathEntity {

    private String path;
    private String name;

    /// 接口需要的权限
    private List<PermissionEntity> permission;
    /// 接口需要的角色
    private List<RoleEntity> role;
}
