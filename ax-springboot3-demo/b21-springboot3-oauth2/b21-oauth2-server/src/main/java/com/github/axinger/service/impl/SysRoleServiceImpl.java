package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.entity.SysRole;
import com.github.axinger.mapper.SysRoleMapper;
import com.github.axinger.service.SysRoleService;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
