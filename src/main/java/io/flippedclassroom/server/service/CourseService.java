package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Course;

import java.util.List;

public interface CourseService extends BaseService<Course> {
    Course findCourseByCourseName(String courseName);

    //查出部分数据
    List<Course> findPartialCourseByUser(long id);
}
