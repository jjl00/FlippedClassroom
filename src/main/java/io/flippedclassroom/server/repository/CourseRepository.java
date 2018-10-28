package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "select * from tb_course where course_name=?1", nativeQuery = true)
    Course findByCourse_name(String course_name);

    @Query(value = "select a.* from tb_course a inner join tb_user_course b  on a.course_id=b.course_id and b.user_id=?1",nativeQuery =true )
    List<Course> findPartialCourseByUser(long id);


}
