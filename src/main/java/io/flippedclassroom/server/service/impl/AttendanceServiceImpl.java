package io.flippedclassroom.server.service.impl;

import io.flippedclassroom.server.entity.Attendance;
import io.flippedclassroom.server.repository.AttendanceRepository;
import io.flippedclassroom.server.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述:
 * 考勤service实现类
 *
 * @author HASEE
 * @create 2018-09-12 11:10
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    AttendanceRepository attendanceRepository;

    @Override
    public Attendance findById(Long id) {
        return attendanceRepository.findOne(id);
    }

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public List<Attendance> save(Iterable<Attendance> iterable) {
        return attendanceRepository.save(iterable);
    }

    @Override
    public void delete(Attendance attendance) {
        attendanceRepository.delete(attendance);
    }

    @Override
    public void deleteById(Long id) {
        attendanceRepository.delete(id);
    }

    @Override
    public void delete(Iterable<Attendance> iterable) {
        attendanceRepository.delete(iterable);
    }

    @Override
    public void deleteAll() {
        attendanceRepository.deleteAll();
    }

    @Override
    public List<Attendance> getAttendancesByCourse(long course_id) {
        return attendanceRepository.getAttendancesByCourse(course_id);
    }

    @Override
    public void updateAttendance(long course_id, long user_id) {
        attendanceRepository.updateAttendance(course_id, user_id);
    }
}