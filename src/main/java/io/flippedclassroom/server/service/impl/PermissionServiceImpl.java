package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Permission;
import io.flippedclassroom.server.repository.PermissionRepository;
import io.flippedclassroom.server.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findOne(id);
    }

    @Override
    public Permission findPermissionByPermissionName(String permissionName) {
        return permissionRepository.findByPermission(permissionName);
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public List<Permission> save(Iterable<Permission> iterable) {
        return permissionRepository.save(iterable);
    }

    @Override
    public void delete(Permission permission) {
        permissionRepository.delete(permission);
    }

    @Override
    public void deleteById(Long id) {
        permissionRepository.delete(id);
    }

    @Override
    public void delete(Iterable<Permission> iterable) {
        permissionRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        permissionRepository.deleteAll();
    }
}
