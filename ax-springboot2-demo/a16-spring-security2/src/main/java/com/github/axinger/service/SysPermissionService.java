package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.domain.SysPermissionEntity;

import java.util.Set;

/**
 * @author xing
 * @description 针对表【sys_permission(权限表)】的数据库操作Service
 * @createDate 2025-07-12 23:48:14
 */
public interface SysPermissionService extends IService<SysPermissionEntity> {

    Set<String> findPermissionCodesByUsername(Object username);
}
