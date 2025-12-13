package com.github.axinger.service.impl;

import com.github.axinger.service.RoleManager;
import org.springframework.stereotype.Component;

@Component
public class RoleManagerImpl implements RoleManager {

    // @Autowired
    // private RoleService roleService;
    //
    // @Autowired
    // private UserRoleService userRoleService;
    //
    // @Autowired
    // private Role2RoleDTOCovert role2RoleDTOCovert;
    //
    // @Override
    // public List<RoleDTO> getRoles(String userId) {
    //     List<UserRole> userRoles = userRoleService.getByUserId(userId);
    //     Set<String> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    //     List<RoleDTO> roleDTOS = role2RoleDTOCovert.covertTargetList2SourceList(roleService.getByIds(roleIds));
    //
    //     // 服务不对外暴露，网关不传token到子服务，这里通过userId获取session，并设置角色。
    //     String tokenValue = StpUtil.getTokenValueByLoginId(userId);
    //
    //     // 为这个token在redis中设置角色，使网关获取更方便
    //     if(StringUtils.isNotEmpty(tokenValue)){
    //         if(CollectionUtils.isEmpty(roleDTOS)){
    //             StpUtil.getTokenSessionByToken(tokenValue).set("ROLES", "");
    //         }else{
    //             List<String> roleNames = roleDTOS.stream().map(RoleDTO::getRoleName).collect(Collectors.toList());
    //             StpUtil.getTokenSessionByToken(tokenValue).set("ROLES", ListUtil.list2String(roleNames));
    //         }
    //     }
    //     return roleDTOS;
    // }
}
