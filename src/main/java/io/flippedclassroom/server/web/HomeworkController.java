package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.*;
import io.flippedclassroom.server.repository.CourseTitleRepository;
import io.flippedclassroom.server.service.HomeworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述:
 * 课后作业控制层
 *
 * @author HASEE
 * @create 2018-09-24 15:01
 */
@RestController
@Slf4j
@Api(tags = "课后作业",description = "实现功能：" +
        "1.获取某课程下的所有章节（标题）" +
        "2.获取某章节（标题）下的所有题目" +
        "3.提交某一题的答案" +
        "4.查看错题集")
public class HomeworkController {
    @Autowired
    HomeworkService homeworkService;
    @Autowired
    CourseTitleRepository courseTitleRepository;
    @ApiOperation(value = "获取某课程下的所有章节（标题）",notes = "传入课程id",httpMethod = "GET")
    @GetMapping("/course/{course_id}/homework/titles")
    public JsonResponse getTitles(@PathVariable long course_id){
       List<CourseTitle> list=courseTitleRepository.findByCourse(new Course().setCourse_id(course_id));
       final List<CourseTitle> result=list.parallelStream().sorted(Comparator.comparing(CourseTitle::getCourse_title_name)).collect(Collectors.toList());
       return new JsonResponse("200","请求成功",new HashMap<String,List<CourseTitle>>(){{put("titles",result);}}) ;
    }
    @ApiOperation(value = "获取某课程下某章节（标题）下的所有作业",notes = "一次性返回多道题",httpMethod = "GET")
    @GetMapping("/course/{course_id}/homework/{title_id}")
    public JsonResponse getHomeworks(@PathVariable("course_id") long course_id,@PathVariable("title_id") long title_id){
        final List<Homework> list=homeworkService.getByCourseAndTitle(title_id,course_id);
        return new JsonResponse("200","请求成功",new HashMap<String,List<Homework>>(){{put("homeworks",list);}}) ;
    }
    @ApiOperation(value = "提交某一章节的做题情况",notes = "传入课后作业的id,表单提交参数携带homework_commit,以及携带token\n"+"homework_commit表示做错题的id,示例: 1,2,3(用逗号分隔)",httpMethod = "POST")
    @PostMapping("/auth/homework/")
    public JsonResponse postHomework( @CurrentUser User user, @RequestParam("homework_commit")String commit){
        String[] array_str=commit.split(",");
        List<Integer> list=new ArrayList<>();
        for(String a:array_str){
            list.add(Integer.parseInt(a));
        }
        homeworkService.commit(list,user);
        return new JsonResponse("200","请求成功",null);
    }
    @ApiOperation(value = "获取错题集",notes = "输入课程id",httpMethod = "GET")
    @GetMapping("/auth/course/{course_id}/homework")
    public JsonResponse getWrong(@PathVariable long course_id,@CurrentUser User user){
        final List<Homework> list=homeworkService.getWrongAnswer(user.getUser_id(),course_id);
        list.parallelStream().sorted(Comparator.comparing(Homework::getHomework_title));
        return new JsonResponse("200","请求成功",new HashMap<String,List<Homework>>(){{put("homeworks",list);}}) ;
    }
}