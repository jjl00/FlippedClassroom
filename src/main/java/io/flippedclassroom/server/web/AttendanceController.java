package io.flippedclassroom.server.web;

import io.flippedclassroom.server.entity.Attendance;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.service.AttendanceService;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * 考勤模块
 *
 * @author HASEE
 * @create 2018-09-12 11:15
 */
@Api(tags = "考勤", description = "目前只实现了获取考勤情况，记录缺课情况功能")
@RestController
@Slf4j
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;

    @GetMapping("/course/{course_id}/attendances")
    @ApiOperation(value = "返回学生考勤情况", httpMethod = "GET")
    @ApiImplicitParam(name = "课程id", required = true)
    public JsonResponse getAttendances(@PathVariable long course_id) {
        List<Attendance> list = attendanceService.getAttendancesByCourse(course_id);
        log.info("取得考勤信息");
        @Data
        class resultSet implements Serializable {
            String user_name;
            int absenteeism;

            resultSet(String user_name, int absenteeism) {
                this.user_name = user_name;
                this.absenteeism = absenteeism;
            }
        }
        List<resultSet> result = new ArrayList();
        Map map = new HashMap();
        for (Attendance a : list) {
            result.add(new resultSet(a.getUser().getUser_name(), a.getAbsenteeism()));
        }
        map.put("course_name", courseService.findById(course_id).getCourse_name());
        map.put("attendance", result);
        return new JsonResponse("200", "返回学生考勤信息", map);
    }

    @PostMapping("/course/{course_id}/attendance/{user_id}")
    @ApiOperation(value = "更新学生考勤情况", httpMethod = "POST")
    public JsonResponse UpdateAttendance(@PathVariable("course_id") long course_id, @PathVariable("user_id") long user_id) {
        attendanceService.updateAttendance(course_id, user_id);
        log.info("用户id:{}在课程id:{}缺勤", user_id, course_id);
        return new JsonResponse("200", "更新考勤情况成功", null);
    }
}