package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.repository.CourseRepository;
import io.flippedclassroom.server.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Course findById(Long id) {
        return courseRepository.findOne(id);
    }

    @Override
    public Course findCourseByCourseName(String courseName) {
        return courseRepository.findByCourse_name(courseName);
    }


//    public List<Course> findPartialCourseByUser(long id) {
//        return jdbcTemplate.query("select course_id,code ,course_name,picture from tb_course where course_id in (select course_id from tb_user_course where user_id =?)", new RowMapper<Course>() {
//            @Override
//            public Course mapRow(ResultSet resultSet, int i) throws SQLException {
//                Course course=new Course();
//                course.setCourse_name(resultSet.getNString("course_name"));
//                course.setPicture(resultSet.getNString("picture"));
//                course.setCode(resultSet.getNString("code"));
//                course.setCourse_id(resultSet.getLong(1));
//                return course;
//            }
//        }, id);
//    }
public List<Course> findPartialCourseByUser(long id) {
    return courseRepository.findPartialCourseByUser(id);
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public List<Course> save(Iterable<Course> iterable) {
        return courseRepository.save(iterable);
    }

    @Override
    public void delete(Course course) {
        courseRepository.delete(course);
    }

    @Override
    public void deleteById(Long id) {
        courseRepository.delete(id);
    }

    @Override
    public void delete(Iterable<Course> iterable) {
        courseRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        courseRepository.deleteAll();
    }


}
