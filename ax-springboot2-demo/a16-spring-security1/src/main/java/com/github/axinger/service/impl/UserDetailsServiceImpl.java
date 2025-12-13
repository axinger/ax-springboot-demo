package com.github.axinger.service.impl;

import com.github.axinger.domain.SysPermissionEntity;
import com.github.axinger.domain.SysRoleEntity;
import com.github.axinger.domain.SysUserEntity;
import com.github.axinger.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserEntity user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 查询用户角色和权限
        List<SysRoleEntity> roles = userMapper.findRolesByUserId(user.getId());
        if (roles != null && !roles.isEmpty()) {
            // 查询每个角色的权限
            roles.forEach(role -> {
                List<SysPermissionEntity> permissions = userMapper.findPermissionsByRoleId(role.getId());
                role.setPermissions(permissions);
            });
            user.setRoles(roles);
        }

        return user;
    }
}
