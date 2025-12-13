package com.github.axinger.service;

import java.util.Set;

public interface PermissionService {
    Set<String> getUserPermissions(Object username);

    String getRequiredPermission(String path, String method);

    void refreshPermissionCache();
}
