package io.flippedclassroom.server.repository;

import io.flippedclassroom.server.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述:
 * 考勤数据访问层
 *
 * @author HASEE
 * @create 2018-09-12 10:32
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    //查询当前课程下学生缺勤情况
    @Query(value = "select * from tb_attendance where course_id=?1", nativeQuery = true)
    List<Attendance> getAttendancesByCourse(long course_id);

    //更新缺勤学生信息
    @Query(value = "update tb_attendance set absenteeism=absenteeism+1 where course_id=?1 and user_id=?2", nativeQuery = true)
    @Modifying
    @Transactional
    //开启事务，否则会报错
    int updateAttendance(long course_id, long user_id);


}