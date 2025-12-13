package com.github.axinger.service;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    // @Autowired
    // private RoleFacade roleFacade;
    //
    // @Autowired
    // private PermissionFacade permissionFacade;
    //
    // @Autowired
    // private ThreadPollConfig threadPollConfig;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null;
        // Object res = StpUtil.getTokenSession().get("PERMS");
        // if (res == null) {
        //     CompletableFuture<List<String>> permFuture = CompletableFuture.supplyAsync(() -> {
        //         // 返回此 loginId 拥有的权限列表
        //         List<PermissionDTO> permissions = permissionFacade.getPermissions((String) loginId);
        //
        //         return permissions.stream().map(PermissionDTO::getPath).collect(Collectors.toList());
        //     });
        //     try {
        //         return permFuture.get();
        //     } catch (InterruptedException | ExecutionException e) {
        //         throw new RuntimeException(e);
        //     }
        // }
        // String paths = (String) res;
        // System.out.println(paths);
        // return ListUtil.string2List(paths);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
        // Object res = StpUtil.getTokenSession().get("ROLES");
        // if (res == null) {
        //     CompletableFuture<List<String>> roleFuture = CompletableFuture.supplyAsync(() -> {
        //         // 返回此 loginId 拥有的权限列表
        //         List<RoleDTO> roles = roleFacade.getRoles((String) loginId);
        //
        //         return roles.stream().map(RoleDTO::getRoleName).collect(Collectors.toList());
        //     }, threadPollConfig.USER_ROLE_PERM_THREAD_POOL);
        //     try {
        //         return roleFuture.get();
        //     } catch (InterruptedException | ExecutionException e) {
        //         throw new RuntimeException(e);
        //     }
        // }
        // String roleNames = (String) res;
        // System.out.println(roleNames);
        // return ListUtil.string2List(roleNames);
    }
}
