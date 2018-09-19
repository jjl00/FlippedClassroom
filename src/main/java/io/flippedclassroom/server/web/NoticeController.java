package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.*;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.repository.UserNoticeRepository;
import io.flippedclassroom.server.service.CourseService;
import io.flippedclassroom.server.service.NoticeService;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.AssertUtils;
import io.flippedclassroom.server.util.DateUtils;
import io.flippedclassroom.server.util.ThreadLocalUtil;
import io.flippedclassroom.server.util.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 * 公告模块
 *
 * @author HASEE
 * @create 2018-09-18 9:31
 */
@Api(value = "通知",description = "使用websocket实现服务器与客户端异步通信")
@RestController
@Slf4j
public class NoticeController {

    @Autowired
    NoticeService noticeService;
    @Autowired
    UserService userService;
    @Autowired
    SimpMessagingTemplate messagingTemplate;
    @Autowired
    RedisService redisService;
    @Autowired
    CourseService courseService;
    @Autowired
    UserNoticeRepository userNoticeRepository;

    @ApiOperation(value = "获取某课程下的通知",tags = "获取某课程下的通知")
    @SubscribeMapping("/course/{course_id}")
    public List<Notice> getNotice(@DestinationVariable("course_id")long course_id,@Header(value = "Authorization") String token) {
        log.info("订阅成功");
        User user=null;
        try {
            user=tokenAuthorization(token);
        } catch (Http400BadRequestException e) {
            e.printStackTrace();
        }
        List<Notice> list=noticeService.getUnReadNotice(course_id,user.getUser_id());
        list.forEach((a)->a.setCourse(null));
        return list;
    }

//   Token 认证
    private User tokenAuthorization(String token) throws Http400BadRequestException {
        log.info("进入自定义 Token 校验！");
        String username = TokenUtils.getUsername(token);
        AssertUtils.assertNotNUllElseThrow(username, () -> new Http400BadRequestException("Token 内容非法！"));
        String redisCheck = redisService.get(username);
        User user = userService.findUserByUsername(username);

        AssertUtils.assertEqualsElseThrow(redisCheck, token, () -> new Http400BadRequestException("Redis校验失败！Token 可能已过期，请重新获取！"));
        AssertUtils.assertNotNUllElseThrow(user, () -> new Http400BadRequestException("Token 内容非法！"));
        // 开始进行校验
        boolean result = TokenUtils.verify(token, username, user.getPassword());
        // 检查校验结果
        AssertUtils.assertTrueElseThrow(result, () -> new Http400BadRequestException("Token 校验失败！"));
        redisService.saveWithExpire(token, username, DateUtils.expire(), TimeUnit.MILLISECONDS);
        return user;
   }


    @ApiOperation(value = "发送公告",tags = "发送公告")
    @MessageMapping("/course/{course_id}")
    public void postNotice(Notice notice,@DestinationVariable("course_id") long course_id){
        log.info("开始发送");
        messagingTemplate.convertAndSend("/topic/course/"+course_id,notice);
        Notice new_notice=noticeService.save(notice);
        List<UserNotice> list=new ArrayList<>();
        courseService.findById(course_id).getUserList().forEach((a)->{
            list.add(new UserNotice().setUser(a).setNotice(new_notice));
        });
        userNoticeRepository.save(list);
    }

    @ApiOperation(value = "将指定id消息指定为已读",tags = "将指定id消息指定为已读")
    @PutMapping("/auth/notice/{notice_id}")
    public JsonResponse updateNotice(@PathVariable long notice_id,@CurrentUser User user){
        noticeService.updateNotice(notice_id,user.getUser_id());
        return new JsonResponse("200","成功将消息更新为已读",null);
    }

}