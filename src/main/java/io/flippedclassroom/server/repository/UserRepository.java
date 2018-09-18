package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    @Query(value = "select * from tb_user where user_name=?1", nativeQuery = true)
    User findByUser_name(String user_name);
}
