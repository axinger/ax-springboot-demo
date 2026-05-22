package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.entity.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色 Mapper 接口
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询角色的权限列表
     */
    @Select("SELECT p.* FROM sys_permission p " +
            "INNER JOIN sys_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId} AND p.deleted = 0 AND p.enabled = 1")
    List<com.github.axinger.entity.SysPermission> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}
