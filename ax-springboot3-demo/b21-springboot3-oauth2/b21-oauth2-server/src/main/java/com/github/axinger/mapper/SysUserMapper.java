package com.github.axinger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.axinger.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户 Mapper 接口
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户（含角色信息）
     */
    @Select("SELECT u.* FROM sys_user u WHERE u.username = #{username} AND u.deleted = 0")
    SysUser selectByUsername(@Param("username") String username);

    /**
     * 查询用户的角色列表
     */
    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted = 0 AND r.enabled = 1")
    List<com.github.axinger.entity.SysRole> selectRolesByUserId(@Param("userId") Long userId);
}
