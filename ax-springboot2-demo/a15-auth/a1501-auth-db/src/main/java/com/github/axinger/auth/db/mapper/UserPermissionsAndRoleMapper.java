package com.github.axinger.auth.db.mapper;

import com.github.axinger.auth.db.dto.ApiPermissionDTO;
import com.github.axinger.auth.db.dto.PermissionRulesVO;
import com.github.axinger.auth.db.dto.UserPermissionsDTO;
import com.github.axinger.auth.db.dto.UsersRoleDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserPermissionsAndRoleMapper {

    List<PermissionRulesVO> getAllPermissionRules();

    List<PermissionRulesVO> getAllRoleRules();


    List<UserPermissionsDTO> getUserPermissions(@Param("userId") Object userId);

    List<UsersRoleDTO> getUserRoles(@Param("userId") Object userId);

    List<ApiPermissionDTO> getUserApiPermissionsAndRole(@Param("userId") Object userId);
}
