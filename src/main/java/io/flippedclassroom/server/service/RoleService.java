package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Role;

import java.util.List;

public interface RoleService extends BaseService<Role> {
    Role findRoleByRoleName(String role);


}
