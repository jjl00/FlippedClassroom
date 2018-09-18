package io.flippedclassroom.server.service;

import io.flippedclassroom.server.entity.Attendance;

import java.util.List;

public interface AttendanceService extends BaseService<Attendance> {
    List<Attendance> getAttendancesByCourse(long course_id);

    void updateAttendance(long course_id, long user_id);
}
