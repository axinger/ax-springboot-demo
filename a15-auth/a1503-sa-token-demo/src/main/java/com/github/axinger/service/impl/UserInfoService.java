package com.github.axinger.service.impl;

import cn.dev33.satoken.model.wrapperInfo.SaDisableWrapperInfo;
import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.ObjUtil;
import com.github.axinger.auth.db.dto.UserPermissionsDTO;
import com.github.axinger.auth.db.dto.UsersRoleDTO;
import com.github.axinger.auth.db.entity.SysUsersEntity;
import com.github.axinger.auth.db.mapper.GetPermissionsMapper;
import com.github.axinger.auth.db.service.SysUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService implements StpInterface {
    private final SysUsersService sysUsersService;
    private final GetPermissionsMapper getPermissionsMapper;


    public SysUsersEntity getUserInfo(String username, String password) {

        SysUsersEntity sysUsersEntity = sysUsersService.lambdaQuery()
                .eq(SysUsersEntity::getUsername, username)
                .last("limit 1")
                .one();

        if (ObjUtil.isEmpty(sysUsersEntity)) {
            throw new RuntimeException("用户不存在");
        }

        if (username.equals(sysUsersEntity.getUsername())) {
            if (!password.equals(sysUsersEntity.getPassword())) {
                throw new RuntimeException("密码错误");
            }
            return sysUsersEntity;
        }

        throw new RuntimeException("用户不存在");
    }


    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的权限码集合
     */
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = (Long) loginId;
        List<UserPermissionsDTO> list = getPermissionsMapper.getUserPermissions(userId);
        return list.stream().map(UserPermissionsDTO::getCode).toList();

    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = (Long) loginId;
        List<UsersRoleDTO> list = getPermissionsMapper.getUserRoles(userId);
        return list.stream().map(UsersRoleDTO::getCode).toList();
    }

    /**
     * 返回指定账号 id 是否被封禁
     *
     * @param loginId 账号id
     * @param service 业务标识符
     * @return 描述该账号是否封禁的包装信息对象
     */
    public SaDisableWrapperInfo isDisabled(Object loginId, String service) {
        return SaDisableWrapperInfo.createNotDisabled();
//        return SaDisableWrapperInfo.createDisabled(1000,2);
    }

}
