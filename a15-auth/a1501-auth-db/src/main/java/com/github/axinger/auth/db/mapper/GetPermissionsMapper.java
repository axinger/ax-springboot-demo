package com.github.axinger.auth.db.mapper;

import com.github.axinger.auth.db.dto.ApiPermissionDTO;
import com.github.axinger.auth.db.dto.PermissionRulesVO;
import com.github.axinger.auth.db.dto.UserPermissionsDTO;
import com.github.axinger.auth.db.dto.UsersRoleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GetPermissionsMapper {

    List<PermissionRulesVO> getAllPermissionRules();

    List<PermissionRulesVO> getAllRoleRules();

    List<UsersRoleDTO> getUserRoles(@Param("userId") Long userId);

    List<UserPermissionsDTO> getUserPermissions(@Param("userId") Long userId);

    List<ApiPermissionDTO> getUserApiPermissions(@Param("userId") Long userId);
}
