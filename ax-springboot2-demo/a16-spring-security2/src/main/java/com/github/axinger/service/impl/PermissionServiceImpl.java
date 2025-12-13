package com.github.axinger.service.impl;

import com.github.axinger.domain.SysPermissionEntity;
import com.github.axinger.service.PermissionService;
import com.github.axinger.service.SysPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PermissionServiceImpl implements PermissionService {

    // 缓存路径-权限映射关系
    private final Map<String, String> pathPermissionCache = new ConcurrentHashMap<>();
    @Autowired
    private SysPermissionService sysPermissionService;

    @PostConstruct
    public void initPathPermissionCache() {

        List<SysPermissionEntity> permissions = sysPermissionService.lambdaQuery()
//                .eq(SysPermissionEntity::getType, 3) // API类型权限
                .eq(SysPermissionEntity::getStatus, 1)
                .eq(SysPermissionEntity::getIsDeleted, 0)
                .list();

        for (SysPermissionEntity permission : permissions) {
            if (StringUtils.isNotBlank(permission.getPath())) {
                pathPermissionCache.put(permission.getPath(), permission.getCode());
            }
        }
    }

    @Override
    public Set<String> getUserPermissions(Object username) {
        return sysPermissionService.findPermissionCodesByUsername(username);
    }

    @Override
    public String getRequiredPermission(String path, String method) {
        // 这里可以根据method(GET/POST等)进一步细化权限控制
        // 例如: path + ":" + method.toLowerCase()
        return pathPermissionCache.get(path);
    }

    @Override
    public void refreshPermissionCache() {
        pathPermissionCache.clear();
        initPathPermissionCache();
    }
}
