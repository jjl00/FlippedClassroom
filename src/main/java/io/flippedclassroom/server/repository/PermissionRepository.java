package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value = "select * from tb_permission where permission=?1", nativeQuery = true)
    Permission findByPermission(String permission);
}
