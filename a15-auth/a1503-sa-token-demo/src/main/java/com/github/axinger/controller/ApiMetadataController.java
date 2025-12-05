package com.github.axinger.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.LambdaUtil;
import com.axing.common.response.dto.Result;
import com.github.axinger.auth.db.dto.ApiPermissionDTO;
import com.github.axinger.auth.db.dto.PermissionRulesVO;
import com.github.axinger.auth.db.dto.UserPermissionsDTO;
import com.github.axinger.auth.db.dto.UsersRoleDTO;
import com.github.axinger.auth.db.entity.SysUsersEntity;
import com.github.axinger.auth.db.mapper.UserPermissionsAndRoleMapper;
import com.github.axinger.dto.ApiMetadataDTO;
import com.github.axinger.service.impl.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/// ApiMetadataController (API管理 - 用于前端动态生成菜单和权限控制)
@Slf4j
@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class ApiMetadataController {

    private final UserPermissionsAndRoleMapper userPermissionsAndRoleMapper;
    private final UserInfoService userInfoService;

    /**
     * 获取所有API元数据 - 需要api:manage权限
     */
    @GetMapping("/apis")
    public Result<?> getAllApis() {
        // 业务逻辑实现
        List<PermissionRulesVO> list = userPermissionsAndRoleMapper.getAllPermissionRules();
        return Result.success(list);
    }

    /**
     * 获取当前用户的API权限 - 不需要特殊权限，只需登录
     */
    @GetMapping("/permissions")
    public Result<?> getUserApiPermissions() {
        // 业务逻辑实现
        Object userId = Convert.toLong(StpUtil.getExtra(LambdaUtil.getFieldName(SysUsersEntity::getId)));
        return Result.success(userPermissionsAndRoleMapper.getUserApiPermissionsAndRole(userId));
    }

    /**
     * 获取菜单和权限配置 - 不需要特殊权限，只需登录
     */
    @GetMapping("/menu-config")
    public Result<?> getMenuConfig() {
        // 业务逻辑实现
        return Result.success("成功查看数据");
    }

    @PostMapping("/isHit2")
    public Result<?> isHit2(@RequestBody ApiMetadataDTO.HitDTO dto) {
        Map<String, Object> result = new HashMap<>();
        String path = dto.getPath();

        long userId = StpUtil.getLoginIdAsLong();

        List<ApiPermissionDTO> userApiPermissionsAndRole = userPermissionsAndRoleMapper.getUserApiPermissionsAndRole(userId);

        ApiPermissionDTO permissionDTO = userApiPermissionsAndRole.stream()
                .filter(apiPermissionDTO -> path.equals(apiPermissionDTO.getPath()))
                .findFirst()
                .orElse(null);
        result.put("api", path);
        result.put("apiPermission", permissionDTO);
        if (null == permissionDTO) {
            result.put("error", "未找到权限");
            return Result.success(result);
        }

        {
            String[] permissionArray = Convert.toStrArray(permissionDTO.getPermissionCode());
            //void checkPermissionOr 校验：当前账号是否含有指定权限标识 [指定多个，只要其一验证通过即可]
            //boolean hasPermissionOr 判断：当前账号是否含有指定权限标识 [指定多个，只要其一验证通过即可]
            boolean isHit = StpUtil.hasPermissionOr(permissionArray);
            log.info("权限是否命中path={},strArray{},isHit={}", path, permissionArray, isHit);
            if (isHit) {
                result.put("permissionHit", "权限命中");
                result.put("permissionArray", permissionArray);
                return Result.success(result);
            }
        }


        {

            String[] roleArray = Convert.toStrArray(permissionDTO.getRoleCodes());
            boolean isHit = StpUtil.hasRoleOr(roleArray);
            log.info("角色是否命中path={},strArray{},isHit={}", path, roleArray, isHit);
            if (isHit) {
                result.put("roleHit", "角色命中");
                result.put("roleArray", roleArray);
                return Result.success(result);
            }
        }

        // 业务逻辑实现
        return Result.success(result);
    }

    /**
     * 指定的api,当前用户是否有权限或者角色 - 不需要特殊权限，只需登录
     */
    @PostMapping("/isHit")
    public Result<?> isHit(@RequestBody ApiMetadataDTO.HitDTO dto) {
        Map<String, Object> result = new TreeMap<>();
        String path = dto.getPath();
        result.put("api", path);
        long userId = StpUtil.getLoginIdAsLong();
        {

//            List<UserPermissionsDTO> userPermissions = userPermissionsAndRoleMapper.getUserPermissions(userId);
//            result.put("userPermission", userPermissions.stream().map(UserPermissionsDTO::getCode).collect(Collectors.toSet()));
            result.put("userPermission", StpUtil.getPermissionList());

        }

        {
//            List<UsersRoleDTO> userPermissions = userPermissionsAndRoleMapper.getUserRoles(userId);
//            result.put("userRole", userPermissions.stream().map(UsersRoleDTO::getCode).collect(Collectors.toSet()));
            result.put("userRole", StpUtil.getRoleList());
        }
        {
            Set<String> apiPermissionList = userPermissionsAndRoleMapper.getAllPermissionRules()
                    .stream().filter(permissionRulesVO -> path.equals(permissionRulesVO.getPath()))
                    .map(PermissionRulesVO::getCode)
                    .collect(Collectors.toSet());

            result.put("apiPermission", apiPermissionList);

            String[] permissionArray = Convert.toStrArray(apiPermissionList);
            //void checkPermissionOr 校验：当前账号是否含有指定权限标识 [指定多个，只要其一验证通过即可]
            //boolean hasPermissionOr 判断：当前账号是否含有指定权限标识 [指定多个，只要其一验证通过即可]
            boolean isHit = StpUtil.hasPermissionOr(permissionArray);
            log.info("权限是否命中path={},strArray{},isHit={}", path, permissionArray, isHit);

            Collection<String> intersection = CollUtil.intersection(apiPermissionList, StpUtil.getPermissionList());
            result.put("permission命中", intersection);
            if (isHit) {
                result.put("permissionHit", "权限命中");
                return Result.success(result);
            } else {
                result.put("permissionHit", "权限未命中");
            }
        }


        {
            Set<String> apiRoleList = userPermissionsAndRoleMapper.getAllRoleRules()
                    .stream().filter(permissionRulesVO -> path.equals(permissionRulesVO.getPath()))
                    .map(PermissionRulesVO::getCode)
                    .collect(Collectors.toSet());
            result.put("apiRole", apiRoleList);
            String[] roleArray = Convert.toStrArray(apiRoleList);
            boolean isHit = StpUtil.hasRoleOr(roleArray);
            log.info("角色是否命中path={},strArray{},isHit={}", path, roleArray, isHit);
            Collection<String> intersection = CollUtil.intersection(apiRoleList, StpUtil.getRoleList());
            result.put("role命中", intersection);
            if (isHit) {
                result.put("roleHit", "角色命中");

                return Result.success(result);
            } else
                result.put("roleHit", "角色未命中");
        }
        return Result.success(result);
    }
}

