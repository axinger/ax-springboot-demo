package com.github.axinger.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.axinger.entity.SysUser;

import java.util.List;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询用户（含角色信息）
     */
    SysUser findByUsername(String username);

    /**
     * 创建新用户
     */
    boolean createUser(SysUser user);

    /**
     * 为用户分配角色
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
}
