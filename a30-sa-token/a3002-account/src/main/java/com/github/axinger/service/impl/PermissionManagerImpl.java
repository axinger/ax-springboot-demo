package com.github.axinger.service.impl;

import com.github.axinger.service.PermissionManager;
import org.springframework.stereotype.Component;

@Component
public class PermissionManagerImpl implements PermissionManager {

    // @Autowired
    // private PermissionService permissionService;
    //
    // @Autowired
    // private RolePermService rolePermService;
    //
    // @Autowired
    // private UserRoleService userRoleService;
    //
    // @Autowired
    // private Permission2PermissionDTOCovert permissionDTOCovert;
    //
    // @Override
    // public List<PermissionDTO> getPermissions(String userId) {
    //
    //     // 获取用户的角色
    //     List<UserRole> roles = userRoleService.getByUserId(userId);
    //     if (CollectionUtils.isEmpty(roles)) {
    //         handleUserPermSession(userId, null);
    //     }
    //
    //     Set<String> roleIds = roles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    //
    //     List<RolePerm> rolePerms = rolePermService.getByRoleIds(roleIds);
    //
    //     if (CollectionUtils.isEmpty(rolePerms)) {
    //         handleUserPermSession(userId, null);
    //     }
    //
    //     Set<String> permIds = rolePerms.stream().map(RolePerm::getPermId).collect(Collectors.toSet());
    //     List<PermissionDTO> perms = permissionDTOCovert.covertTargetList2SourceList(permissionService.getByIds(permIds));
    //
    //     handleUserPermSession(userId, perms);
    //     return perms;
    // }
    //
    //
    // private void handleUserPermSession(String userId, List<PermissionDTO> perms) {
    //     // 通过userId获取session，并设置权限
    //     String tokenValue = StpUtil.getTokenValueByLoginId(userId);
    //
    //     if (StringUtils.isNotEmpty(tokenValue)) {
    //         // 为了防止没有权限的用户多次进入到该接口，没权限的用户在redis中存入空字符串
    //         if (CollectionUtils.isEmpty(perms)) {
    //             StpUtil.getTokenSessionByToken(tokenValue).set("PERMS", "");
    //         } else {
    //             List<String> paths = perms.stream().map(PermissionDTO::getPath).collect(Collectors.toList());
    //             StpUtil.getTokenSessionByToken(tokenValue).set("PERMS", ListUtil.list2String(paths));
    //         }
    //     }
    // }
}
