package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.UserNotice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述:
 * 插入通知
 *
 * @author HASEE
 * @create 2018-09-19 13:06
 */
public interface UserNoticeRepository extends JpaRepository<UserNotice,Long> {

}