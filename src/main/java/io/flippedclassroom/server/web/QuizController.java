package io.flippedclassroom.server.web;//package io.flippedclassroom.server.web;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
/**
 * @Author HASEE
 * @Description
 * 设计思路如下：
 *      1.老师发送开始随堂测试请求
 *      2.使用线程池给当前课堂上的每个学生推送指定的随堂测试题
 *      3.老师那里使用门闩，进行阻塞（给定一个阻塞时间）
 *      4.等所有学生提交完之后，进行统计结果
 * @Date 15:10 2018/9/22
 * @Param
 * @return
 **/
@Api(tags = "随堂测试", description = "目前包括：开启测试入口、关闭测试入口、获取全部测试题、新增测试题、提交单个测试题的答案、查看统计结果")
@RestController
public class QuizController {


}
