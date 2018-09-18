package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.repository.RoleRepository;
import io.flippedclassroom.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findById(Long id) {
        return roleRepository.findOne(id);
    }

    @Override
    public Role findRoleByRoleName(String role) {
        return roleRepository.findByRole(role);
    }


    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> save(Iterable<Role> iterable) {
        return roleRepository.save(iterable);
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.delete(id);
    }

    @Override
    public void delete(Iterable<Role> iterable) {
        roleRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        roleRepository.deleteAll();
    }
}
