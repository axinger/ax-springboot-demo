package com.github.axinger.auth.db.dto;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiPermissionDTO {
    private String path;
    private String method;
    private String permissionCode;
    private Long userId;
    private String username;
    private List<Long> roleIds;
    private List<String> roleNames;
    private List<String> roleCodes;

    // Getters and setters

    // 处理逗号分隔的字符串到列表的转换
    public void setRoleIds(String roleIdsStr) {
        if (StringUtils.isNotBlank(roleIdsStr)) {
            this.roleIds = Arrays.stream(roleIdsStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
    }

    public void setRoleNames(String roleNamesStr) {
        if (StringUtils.isNotBlank(roleNamesStr)) {
            this.roleNames = Arrays.stream(roleNamesStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }

    public void setRoleCodes(String roleCodesStr) {
        if (StringUtils.isNotBlank(roleCodesStr)) {
            this.roleCodes = Arrays.stream(roleCodesStr.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }
    }
}
