package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.domain.SysPermissionEntity;
import com.github.axinger.domain.SysUserEntity;

import java.util.List;

/**
 * @author xing
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service
 * @createDate 2025-07-12 23:48:24
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 根据用户名查询用户及其角色权限信息
     */
    SysUserEntity loadUserByUsername(String username);

    /**
     * 根据用户ID查询用户及其角色权限信息
     */
    SysUserEntity findUserWithRolesAndPermissionsById(Integer id);

    /**
     * 查询用户菜单树(只包含菜单类型权限)
     */
    List<SysPermissionEntity> findUserMenuTree(Integer userId);
}
