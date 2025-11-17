package com.github.axinger.auth.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.axinger.auth.db.entity.SysRolePermissionsEntity;
import com.github.axinger.auth.db.service.SysRolePermissionsService;
import com.github.axinger.auth.db.mapper.SysRolePermissionsMapper;
import org.springframework.stereotype.Service;

/**
* @author xing
* @description 针对表【sys_role_permissions(角色权限关联表)】的数据库操作Service实现
* @createDate 2025-12-05 17:37:25
*/
@Service
public class SysRolePermissionsServiceImpl extends ServiceImpl<SysRolePermissionsMapper, SysRolePermissionsEntity>
    implements SysRolePermissionsService{

}




