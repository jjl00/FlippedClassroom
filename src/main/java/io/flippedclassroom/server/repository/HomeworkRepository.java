package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.Homework;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;


import java.util.List;

/**
 * 描述:
 * 课后作业数据访问层
 *
 * @author HASEE
 * @create 2018-09-23 13:31
 */
public interface HomeworkRepository extends JpaRepository<Homework,Integer> {
     @Query(value = "select * from tb_homework where course_title_id=?1 and course_id=?2",nativeQuery = true)
     List<Homework> findByTitleAndCourseId(long homework_title, long course_id);

     @Query(value = "select homework_title from tb_homework where course_id=?1",nativeQuery = true)
     List<String> findTitles(long course_id);



}