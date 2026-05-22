package com.github.axinger.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.entity.SysPermission;
import com.github.axinger.mapper.SysPermissionMapper;
import com.github.axinger.service.SysPermissionService;
import org.springframework.stereotype.Service;

/**
 * 权限服务实现类
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
}
