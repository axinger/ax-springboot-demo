package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.entity.SysRole;
import com.github.axinger.entity.SysUser;
import com.github.axinger.entity.SysUserRole;
import com.github.axinger.mapper.SysUserMapper;
import com.github.axinger.mapper.SysUserRoleMapper;
import com.github.axinger.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser findByUsername(String username) {
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user != null) {
            List<SysRole> roles = sysUserMapper.selectRolesByUserId(user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        user.setAccountNonExpired(1);
        user.setAccountNonLocked(1);
        user.setCredentialsNonExpired(1);
        return save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoles(Long userId, List<Long> roleIds) {
        // 先删除原有角色关联
        sysUserRoleMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
        );
        // 添加新角色关联
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            sysUserRoleMapper.insert(userRole);
        }
        return true;
    }
}
