package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.CourseTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 描述:
 * 课程标题dao
 *
 * @author HASEE
 * @create 2018-09-28 15:36
 */
public interface CourseTitleRepository extends JpaRepository<CourseTitle,Long> {
    List<CourseTitle> findByCourse(Course course);
}