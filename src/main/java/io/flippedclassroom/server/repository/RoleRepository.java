package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select * from tb_role where role=?1", nativeQuery = true)
    Role findByRole(String role);


}
