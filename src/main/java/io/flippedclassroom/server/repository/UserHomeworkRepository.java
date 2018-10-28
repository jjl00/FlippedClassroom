package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Homework;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.UserHomework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 描述:
 * 用户与课后作业的数据层
 *
 * @author HASEE
 * @create 2018-09-23 13:49
 */
public interface UserHomeworkRepository extends JpaRepository<UserHomework,Integer> {
    @Query(value = "select tb_homework.homework_id from tb_user_homework,tb_homework " +
            " where tb_homework.course_id=?1 and tb_user_homework.user_id=?2 and tb_user_homework.homework_id=tb_homework.homework_id",nativeQuery = true)
    List<Integer> findWrongAnswer(long course_id, long user_id);

    UserHomework findByUserAndHomework(User user, Homework homework);
}