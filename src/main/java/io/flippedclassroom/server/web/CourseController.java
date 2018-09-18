package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.util.Const.Const;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@Slf4j
@Api(tags = "课程", description = "课程相关的所有内容都需要 Token 验证。目前包括：列出所有课程、查找课程、更新课程、删除课程、创建课程、加入课程")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @GetMapping("/auth/courses")
    @ApiOperation(value = "课程列表", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
    @ApiResponses(
            @ApiResponse(code = 200, message = "自定义的返回格式：\n{\n" +
                    "  \"courses\" : [ {\n" +
                    "    \"id\" : 1,\n" +
                    "    \"name\" : \"数学\",\n" +
                    "    \"major\" : \"数学专业\",\n" +
                    "    \"picture\" : \"https://fc.xd.style/course/1/picture\",\n" +
                    "    \"count\" : 2,\n" +
                    "    \"code\" : \"xust1\"\n" +
                    "  } ],\n" +
                    "  \"status\" : \"SUCCESS\"\n" +
                    "}")
    )
    public JsonResponse getCourses(@ApiIgnore @CurrentUser User user) {
        Map<String, Object> map = new HashMap<>();
        @Data
        @Accessors(chain = true)
        class ResultSet{
            String course_name;
            String code;
            String picture;
            long course_id;
        }
        List<Course> list= courseService.findPartialCourseByUser(user.getUser_id());
        List<ResultSet> result=list.parallelStream().map((a)-> {
            return new ResultSet().setCode(a.getCode()).setCourse_name(a.getCourse_name())
                    .setPicture(a.getPicture()).setCourse_id(a.getCourse_id());
        }).collect(Collectors.toList());
        map.put("courses",result);
        return new JsonResponse("200", "请求成功", map);
    }

    @GetMapping(value = "course/{course_id}")
    @ApiOperation(value = "通过课程 ID 查找课程", notes = "需要以 课程ID 构建 URL，例如 https://fc.xd.style/course/1", httpMethod = "GET")
    @ApiResponses(
            @ApiResponse(code = 200, message = "课程实体类：\n{\n" +
                    "  \"id\" : 1,\n" +
                    "  \"name\" : \"数学\",\n" +
                    "  \"major\" : \"数学专业\",\n" +
                    "  \"picture\" : \"https://fc.xd.style/course/1/picture\",\n" +
                    "  \"count\" : 2,\n" +
                    "  \"code\" : \"xust1\"\n" +
                    "}")
    )
    public JsonResponse getCourse(@PathVariable("course_id") Long course_id) throws Throwable {
        Course course = courseService.findById(course_id);
        AssertUtils.assertNotNull(new Http400BadRequestException("未找到该课程"), course);
        System.out.println(course);
        return new JsonResponse("200", "请求成功", course.toString());
    }

    @PutMapping(value = "course/{course_id}")
    @ApiOperation(value = "更新课程信息", notes = "需要以 课程ID 构建 URL，例如 https://fc.xd.style/course/1", httpMethod = "PUT")
    @ApiImplicitParam(name = "course", required = true, value = "课程信息JSON：\n{\n\"name\":\"新的课程名\", \n\"major\":\"新的课程所属专业\"\n}")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse updateCourse(@PathVariable("course_id") long course_id, @RequestBody Course course) throws Http400BadRequestException {
        Course newCourse = courseService.findById(course_id);
        AssertUtils.assertNotNUllElseThrow(newCourse, () -> new Http400BadRequestException("未找到该课程"));
        newCourse.setCourse_name(course.getCourse_name());
        newCourse.setMajor(course.getMajor());
        courseService.save(newCourse);
        return new JsonResponse("200", "成功更新课程信息！", null);
    }

    @DeleteMapping(value = "/auth/course/{course_id}")
    @ApiOperation(value = "删除课程", notes = "对于学生，是课程中少了一个学生；对于教师，是整个课程没了", httpMethod = "DELETE")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse deleteCourse(@PathVariable Long course_id, @NotNull @ApiIgnore @CurrentUser User user) throws Http400BadRequestException {
        Course course = courseService.findById(course_id);
        String role = user.getRole().getRole();
        AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("未找到该课程"));
        log.info("用户{} --角色{} 删除了课程{} ", user.getUser_name(), role, course.getCourse_name());
        if (role.equals(Const.Student)) {        // 学生删除课程逻辑
            user.getCourseList().remove(course);
            userService.save(user);
            return new JsonResponse("200", "学生成功删除所选课程！", null);
        } else {
            courseService.delete(course);
            return new JsonResponse("200", "成功删除课程！", null);
        }

    }

    @PostMapping(value = "/auth/course", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加课程", notes = "分为学生添加课程和老师创建课程。传入course对象,如果是老师则创建课程，否则是学生添加课程", httpMethod = "POST")
    @ApiImplicitParam(name = "course", value = "课程信息：\n{\n\"name\":\"课程名\", \n\"major\":\"课程所属专业\"\n}")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse addCourse(@RequestBody Course course, @ApiIgnore @CurrentUser User user) {
        String role = user.getRole().getRole();
        if (role.equals(Const.Student)) {
            user.getCourseList().add(courseService.findById(course.getCourse_id()));
            userService.save(user);
            log.info("学生{}添加了{}课程", user.getUser_name(), course.getCourse_name());
        } else {
            courseService.save(course);
            log.info("老师{}创建了{}课程", user.getUser_name(), course.getCourse_name());
        }
        return new JsonResponse("200", "添加课程成功", null);
    }
}

