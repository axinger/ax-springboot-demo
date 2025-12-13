package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.domain.SysPermissionEntity;
import com.github.axinger.domain.SysRoleEntity;
import com.github.axinger.domain.SysUserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xing
 * @description 针对表【sys_user(系统用户表)】的数据库操作Mapper
 * @createDate 2025-07-12 23:48:24
 * @Entity com.github.axinger.domain.SysUserEntity
 */
public interface SysUserMapper extends BaseMapper<SysUserEntity> {


    /**
     * 根据用户名查询用户及其角色权限信息
     */
    SysUserEntity findUserWithRolesAndPermissionsByUsername(String username);

    /**
     * 根据用户ID查询用户及其角色权限信息
     */
    SysUserEntity findUserWithRolesAndPermissionsById(Integer id);

    /**
     * 查询用户菜单树(只包含菜单类型权限)
     */
    List<SysPermissionEntity> findUserMenuTree(Integer userId);


    SysUserEntity findByUsername(@Param("username") String username);

    List<SysRoleEntity> findRolesByUserId(@Param("userId") Integer userId);

    List<SysPermissionEntity> findPermissionsByRoleId(@Param("roleId") Integer roleId);
}




