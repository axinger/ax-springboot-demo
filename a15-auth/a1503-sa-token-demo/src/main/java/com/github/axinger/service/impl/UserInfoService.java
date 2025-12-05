package com.github.axinger.service.impl;

import cn.dev33.satoken.model.wrapperInfo.SaDisableWrapperInfo;
import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.github.axinger.auth.db.dto.PermissionRulesVO;
import com.github.axinger.auth.db.dto.UserPermissionsDTO;
import com.github.axinger.auth.db.dto.UsersRoleDTO;
import com.github.axinger.auth.db.entity.SysUsersEntity;
import com.github.axinger.auth.db.mapper.UserPermissionsAndRoleMapper;
import com.github.axinger.auth.db.service.SysUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoService implements StpInterface {
    private final SysUsersService sysUsersService;
    private final UserPermissionsAndRoleMapper userPermissionsAndRoleMapper;

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
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = Convert.toLong(loginId);
        List<UserPermissionsDTO> list = userPermissionsAndRoleMapper.getUserPermissions(userId);
        List<String> list1 = list.stream().map(UserPermissionsDTO::getCode).toList();
        log.info("用户权限userId={},list={}", userId, list1);
        return list1;
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = Convert.toLong(loginId);
        List<UsersRoleDTO> list = userPermissionsAndRoleMapper.getUserRoles(userId);
        List<String> list1 = list.stream().map(UsersRoleDTO::getCode).toList();
        log.info("用户角色userId={},list={}", userId, list1);
        return list1;
    }

    /**
     * 返回指定账号 id 是否被封禁
     *
     * @param loginId 账号id
     * @param service 业务标识符
     * @return 描述该账号是否封禁的包装信息对象
     */
    @Override
    public SaDisableWrapperInfo isDisabled(Object loginId, String service) {
        return SaDisableWrapperInfo.createNotDisabled();
//        return SaDisableWrapperInfo.createDisabled(1000,2);
    }

    // 动态获取鉴权规则
    public Map<String, Set<String>> getAuthPermission() {
        // 从数据库获取所有权限规则
        List<PermissionRulesVO> list = userPermissionsAndRoleMapper.getAllPermissionRules();
        Map<String, Set<String>> rules = list.stream()
                .collect(Collectors.groupingBy(
                        PermissionRulesVO::getPath,
                        Collectors.mapping(PermissionRulesVO::getCode, Collectors.toSet())
                ));
        log.info("权限规则={}", rules);
        return rules;
    }

    public Map<String, Set<String>> getAuthRoles() {
        // 从数据库获取所有角色规则
        List<PermissionRulesVO> list = userPermissionsAndRoleMapper.getAllRoleRules();
        Map<String, Set<String>> rules = list.stream()
                .collect(Collectors.groupingBy(
                        PermissionRulesVO::getPath,
                        Collectors.mapping(PermissionRulesVO::getCode, Collectors.toSet())
                ));
        log.info("角色规则={}", rules);
        return rules;
    }
}
