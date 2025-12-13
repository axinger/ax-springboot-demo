package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.domain.SysPermissionEntity;
import com.github.axinger.domain.SysUserEntity;
import com.github.axinger.mapper.SysUserMapper;
import com.github.axinger.service.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xing
 * @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
 * @createDate 2025-07-12 23:48:24
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity>
        implements SysUserService {

    @Override
    public SysUserEntity loadUserByUsername(String username) {
        return getBaseMapper().findUserWithRolesAndPermissionsByUsername(username);
    }

    @Override
    public SysUserEntity findUserWithRolesAndPermissionsById(Integer id) {
        return getBaseMapper().findUserWithRolesAndPermissionsById(id);
    }

    @Override
    public List<SysPermissionEntity> findUserMenuTree(Integer userId) {
        return getBaseMapper().findUserMenuTree(userId);
    }
}




