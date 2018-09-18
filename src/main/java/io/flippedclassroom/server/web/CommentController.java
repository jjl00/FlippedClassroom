package io.flippedclassroom.server.web;


import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.Comment;
import io.flippedclassroom.server.entity.Course;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.CommentService;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.DateUtils;
import io.flippedclassroom.server.util.LogUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.*;

@RestController
@CrossOrigin
@Slf4j
@Api(tags = "评论", description = "目前包括：查看课程评论、添加课程评论、更新课程评论、删除课程评论")
public class CommentController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/course/{courseID}/comment")
    @ApiOperation(value = "查看课程评论", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
    @ApiResponses(
            @ApiResponse(code = 200, message = "自定义的返回格式：{\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"status\" : \"SUCCESS\",\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"comments\" : [ {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 6,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"Hello World!\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:28\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : -1,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"1\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"student\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;}, {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 7,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"这条评论来自老师，回复第一条评论\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:29\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : 6,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"2\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"teacher\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;}, {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 8,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"课上的十分好！（回复上面老师的评论）\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:29\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : 7,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"1\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"student\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;}, {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 9,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"这里是第四条评论，这条评论是顶层评论\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:29\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : -1,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"3\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"student\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;}, {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 10,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"第五：回复第四条评论\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:29\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : 9,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"1\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"student\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;} ]\n" +
                    "}")
    )
    public JsonResponse getComments(@PathVariable Long courseID) {

        Course course = courseService.findById(courseID);
        if (course == null) {
            return new JsonResponse("4xx", "未找到相应的课程号", null);
        } else {
            List<Comment> comments = course.getCommentList();
            comments.parallelStream().forEach(comment -> comment.getUser().setAvatar("https://api.fc.xd.style/avatar"));
//			comments = comments.parallelStream().filter(comment -> comment.getReply().equals(-1L)).collect(Collectors.toList());
            return new JsonResponse("200", "成功", comments);
        }
    }

    @PostMapping(value = "/auth/course/{courseID}/comment", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "评论课程", httpMethod = "POST")
    @ApiImplicitParam(name = "comment", value = "留下一个评论：\n{\n" +
            "\"content\":\"评论内容\",\n " +
            "\"reply\":\"回复评论，不设置默认 -1\"\n" +
            "}")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse postComment(@PathVariable Long courseID, @RequestBody Comment comment, @ApiIgnore @CurrentUser User user) throws Http400BadRequestException {
        Course course = courseService.findById(courseID);
        AssertUtils.assertNotNUllElseThrow(course, () -> new Http400BadRequestException("未找到该课程"));
        Comment newComment = new Comment(comment.getContent());
        newComment.setUser(user).setCourse(course).setReply(comment.getReply()).setComment_date(new Date(System.currentTimeMillis()));
        commentService.save(newComment);
        return new JsonResponse("200", "成功添加评论！", null);
    }

    @GetMapping(value = "/course/{courseID}/comment/{commentId}")
    @ApiOperation(value = "查看某一具体评论", httpMethod = "GET")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "commentId", required = true, value = "如果请求的 commentId 非法，则返回：\n{\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"msg\" : \"评论 id 非法！\",\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"commentsToComment\" : [ ],\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"comment\" : \"\",\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"status\" : \"FAILED\"\n" +
                    "}\n如果请求的评论不是顶级评论（即 reply 字段不是 -1），则返回：\n{\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"msg\" : \"暂不支持查看非顶级评论下的评论\",\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"commentsToComment\" : [ ],\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"comment\" : {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 7,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"这条评论来自老师，回复第一条评论\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:29\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : 6,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"2\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"teacher\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;},\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"status\" : \"FAILED\"\n" +
                    "}\n如果请求的评论是顶级评论，则返回：\n{\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"msg\" : \"成功获取到评论列表！\",\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"commentsToComment\" : [ {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 7,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"这条评论来自老师，回复第一条评论\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:29\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : 6,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"2\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"teacher\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;} ],\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"comment\" : {\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"id\" : 6,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"content\" : \"Hello World!\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"date\" : \"2018-04-02 17:49:28\",\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"reply\" : -1,\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"user\" : {\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"nickname\" : \"1\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"avatar\" : \"https://api.fc.xd.style/avatar\",\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : {\n" +
                    "        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\"role\" : \"student\"\n" +
                    "      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;},\n" +
                    "  &nbsp;&nbsp;&nbsp;&nbsp;\"status\" : \"SUCCESS\"\n" +
                    "}")
    )
    @ApiResponses(
            @ApiResponse(code = 200, message = "自定义的返回格式")
    )
    public JsonResponse getComment(@PathVariable Long courseID, @PathVariable Long commentId) {
        Map<String, Object> map = new HashMap<>();
        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            map.put("comment", "");
            map.put("commentsToComment", Collections.EMPTY_LIST);
            return new JsonResponse("4xx", "未找到相应的评论", map);
        } else {
            if (-1 != comment.getReply()) {
                map.put("comment", comment);
                map.put("commentsToComment", Collections.EMPTY_LIST);
                return new JsonResponse("4xx", "暂不支持查看非顶级评论下的评论", map);
            } else {
                map.put("comment", comment);
                map.put("commentsToComment", commentService.findReplyTo(commentId));
                return new JsonResponse("200", "成功获取到评论列表", map);
            }

        }
    }

    @PutMapping(value = "/comment/{commentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "更新评论", httpMethod = "PUT", notes = "使用 HTTP PUT 方法请求，需要和 POST 一样附带 Request Body")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "comment", dataTypeClass = Comment.class, required = true, value = "\n示例：\n" +
                    "{\n" +
                    "\"content\":\"更新后的评论内容\"\n" +
                    "}")
    )
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse updateComment(@PathVariable Long commentId, @RequestBody @NotNull Comment comment) throws Http400BadRequestException {
        Comment originComment = commentService.findById(commentId);
        AssertUtils.assertNotNUllElseThrow(originComment, () -> new Http400BadRequestException("更新失败！请确保要更新的评论 id 合法！"));
        originComment.setContent(comment.getContent());
        originComment.setComment_date(new Date(System.currentTimeMillis()));
        commentService.save(originComment);
        return new JsonResponse("200", "成功更新评论！", null);
    }

    @DeleteMapping(value = "/comment/{commentId}")
    @ApiOperation(value = "删除评论", httpMethod = "DELETE", notes = "使用 HTTP DELETE 方法请求此 API 即可")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse deleteComment( @PathVariable Long commentId) throws Http400BadRequestException {
        Comment comment = commentService.findById(commentId);
        AssertUtils.assertNotNUllElseThrow(comment, () -> new Http400BadRequestException("删除失败！请确保评论 id 合法！"));
        log.info("删除了--{}--评论 " + comment.getContent());
        commentService.deleteById(commentId);
        return new JsonResponse("200", "成功删除评论！", null);
    }
}
