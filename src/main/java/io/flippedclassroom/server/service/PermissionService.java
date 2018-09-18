package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Permission;

import java.util.List;

public interface PermissionService extends BaseService<Permission> {
    Permission findPermissionByPermissionName(String permissionName);
}
