package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.domain.SysPermissionEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

/**
 * @author xing
 * @description 针对表【sys_permission(权限表)】的数据库操作Mapper
 * @createDate 2025-07-12 23:48:14
 * @Entity com.github.axinger.domain.SysPermissionEntity
 */
public interface SysPermissionMapper extends BaseMapper<SysPermissionEntity> {


    @Select("SELECT p.code FROM sys_permission p " +
            "JOIN sys_role_permission rp ON p.id = rp.permission_id AND rp.is_deleted = 0 " +
            "JOIN sys_user_role ur ON rp.role_id = ur.role_id AND ur.is_deleted = 0 " +
            "JOIN sys_user u ON ur.user_id = u.id AND u.is_deleted = 0 AND u.status = 1 " +
            "WHERE u.username = #{username} AND p.is_deleted = 0 AND p.status = 1")
    Set<String> findPermissionCodesByUsername(@Param("username") String username);

    @Select("SELECT p.* FROM sys_permission p " +
            "WHERE p.path = #{path} AND p.type = 3 AND p.is_deleted = 0 AND p.status = 1")
    SysPermissionEntity findByPath(@Param("path") String path);
}




