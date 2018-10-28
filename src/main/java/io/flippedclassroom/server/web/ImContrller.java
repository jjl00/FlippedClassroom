//package io.flippedclassroom.server.web;
//
//import io.flippedclassroom.server.annotation.CurrentUser;
//import io.flippedclassroom.server.entity.*;
//
//import io.flippedclassroom.server.entity.im.Message;
//import io.flippedclassroom.server.entity.im.MessageType;
//import io.flippedclassroom.server.exception.Http400BadRequestException;
//import io.flippedclassroom.server.repository.UserNoticeRepository;
//import io.flippedclassroom.server.service.*;
//import io.flippedclassroom.server.util.AssertUtils;
//import io.flippedclassroom.server.util.DateUtils;
//import io.flippedclassroom.server.util.ImUtil;
//import io.flippedclassroom.server.util.TokenUtils;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.annotation.SubscribeMapping;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * 描述:
// * 使用网易云信api实现即时通讯
// *
// * @author HASEE
// * @create 2018-10-22 15:05
// */
//@RestController
//@Api(tags = "即时通信",description = "使用网易云信api实现即时通讯")
//@Slf4j
//public class ImContrller {
//    @Autowired
//    CourseService courseService;
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private RedisService redisService;
//    @Autowired
//    private MessageService messageService;
//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;
//    @Autowired
//    private NoticeService noticeService;
//
//    @Autowired
//    private UserNoticeRepository userNoticeRepository;
//
//
//    @MessageMapping(value = "/group/{courseId}")
//    public void dynamicGroup(@DestinationVariable Long courseId, @Header(value = "Authorization") String token, Message msg) throws Http400BadRequestException {
//        User user;
//        try {
//            user = this.redisAuthorization(token);
////            user.setAvatar(Const.showAvatar);
//        } catch (Exception e) {
//            simpMessagingTemplate.convertAndSend("/g/" + courseId, new HashMap<String, Object>(){{
//                put("code", "4xx");
//                put("message", e.getMessage());
//            }});
//            return;
//        }
//        Message message = new Message(msg.getContent(), MessageType.GROUP);
//        message.setUser(user);
//        message.setCourseId(courseId);
//        messageService.save(message);
//        simpMessagingTemplate.convertAndSend("/g/" + courseId, new HashMap<String, Object>(){{
//            put("code", "200");
//            put("message", message);
//        }});
//    }
//
//    private User redisAuthorization(String token) throws Http400BadRequestException {
//        String username = redisService.get(token);
//        if (username != null) {
//            return userService.findUserByUsername(username);
//        } else {
//            return this.tokenAuthorization(token);
//        }
//    }
//
//
//
//
//
//    @ApiOperation(value = "获取某课程下的通知",tags = "获取某课程下的通知")
//    @SubscribeMapping("/course/{course_id}")
//    public List<Notice> getNotice(@DestinationVariable("course_id")long course_id,@Header(value = "Authorization") String token) {
//        log.info("订阅成功");
//        User user=null;
//        try {
//            user=tokenAuthorization(token);
//        } catch (Http400BadRequestException e) {
//            e.printStackTrace();
//        }
//        List<Notice> list=noticeService.getUnReadNotice(course_id,user.getUser_id());
//        list.forEach((a)->a.setCourse(null));
//        return list;
//    }
//
//    //   Token 认证
//    private User tokenAuthorization(String token) throws Http400BadRequestException {
//        log.info("进入自定义 Token 校验！");
//        String username = TokenUtils.getUsername(token);
//        AssertUtils.assertNotNUllElseThrow(username, () -> new Http400BadRequestException("Token 内容非法！"));
//        String redisCheck = redisService.get(username);
//        User user = userService.findUserByUsername(username);
//
//        AssertUtils.assertEqualsElseThrow(redisCheck, token, () -> new Http400BadRequestException("Redis校验失败！Token 可能已过期，请重新获取！"));
//        AssertUtils.assertNotNUllElseThrow(user, () -> new Http400BadRequestException("Token 内容非法！"));
//        // 开始进行校验
//        boolean result = TokenUtils.verify(token, username, user.getPassword());
//        // 检查校验结果
//        AssertUtils.assertTrueElseThrow(result, () -> new Http400BadRequestException("Token 校验失败！"));
//        redisService.saveWithExpire(token, username, DateUtils.expire(), TimeUnit.MILLISECONDS);
//        return user;
//    }
//
//
//    @ApiOperation(value = "发送公告",tags = "发送公告")
//    @MessageMapping("/course/{course_id}")
//    public void postNotice(Notice notice, @DestinationVariable("course_id") long course_id){
//        log.info("开始发送");
//        simpMessagingTemplate.convertAndSend("/topic/course/"+course_id,notice);
//        Notice new_notice=noticeService.save(notice);
//        List<UserNotice> list=new ArrayList<>();
//        courseService.findById(course_id).getUserList().forEach((a)->{
//            list.add(new UserNotice().setUser(a).setNotice(new_notice));
//        });
//        userNoticeRepository.save(list);
//    }
//
//    @ApiOperation(value = "将指定id消息指定为已读",tags = "将指定id消息指定为已读")
//    @PutMapping("/auth/notice/{notice_id}")
//    public JsonResponse updateNotice(@PathVariable long notice_id,@CurrentUser User user){
//        noticeService.updateNotice(notice_id,user.getUser_id());
//        return new JsonResponse("200","成功将消息更新为已读",null);
//    }
//
//
//
//
////    @GetMapping("/auth/im/user")
////    @ApiOperation(value = "创建网易云信通信id,并返回token",notes = "传入认证token，返回网易云信认证token")
////    public JsonResponse getToken(@CurrentUser User user){
////        String url = "https://api.netease.im/nimserver/user/refreshToken.action";
////        List<NameValuePair> pairs = new ArrayList<>();
////        pairs.add(new BasicNameValuePair("accid", user.getUser_id()+""));
////        JSONObject jsonObject=null;
////
////        try {
////            jsonObject=ImUtil.commonHttpRequest(url,pairs);
////        } catch (Exception e) {
////            return new JsonResponse("5xx","出现异常",null);
////        }
////        JSONObject info=(JSONObject)jsonObject.get("info");
////        return new JsonResponse(jsonObject.get("code").toString(),"请求成功",info.toString());
////    }
////
////
////    @PostMapping("/auth/im/team/{course_id}")
////    @ApiOperation(value = "创建群后返回群id即tid",notes = "使用表单数据,参数如下:\n t_name=dev表示群的名字,members=[1,2,3]创建群时邀请的人的id\n" +
////            "msg=welcome 表示邀请时的消息,magree表示管理后台建群时，0不需要被邀请人同意加入群，1需要被邀请人同意才可以加入群。其它会返回414," +
////            "joinmode：群建好后，sdk操作时，0不用验证，1需要验证,2不允许任何人加入。其它返回414")
////    public JsonResponse createTeam(@CurrentUser User user,@RequestParam("tname")String tname, @RequestParam("members")String[] members,@RequestParam("msg")String msg,@RequestParam("magree")int magree,@RequestParam("joinmode")int joinmode,@PathVariable("course_id")long course_id)throws Exception{
////        Course course=courseService.findById(course_id);
////        String url="https://api.netease.im/nimserver/team/create.action";
////        List<NameValuePair> pairs = new ArrayList<>();
////        pairs.add(new BasicNameValuePair("tname", tname));
////        pairs.add(new BasicNameValuePair("owner",user.getUser_id()+""));
////        pairs.add(new BasicNameValuePair("members",new JSONArray(members).toString()));
////        pairs.add(new BasicNameValuePair("msg",msg));
////        pairs.add(new BasicNameValuePair("magree",magree+""));
////        pairs.add(new BasicNameValuePair("joinmode",joinmode+""));
////        JSONObject jsonObject=ImUtil.commonHttpRequest(url,pairs);
////        courseService.save(course.setTeam_id(jsonObject.get("tid").toString()));
////        return new JsonResponse( jsonObject.get("code").toString(),"请求成功",jsonObject.get("tid").toString());
////    }
////
////    @PostMapping("/auth/im/msg")
////    @ApiOperation(value = "发送消息",notes = "传入表单参数: ope表示0：点对点个人消息，1：群消息（高级群），其他返回414\n" +
////            "to表示ope==0是表示accid即用户id，ope==1表示tid即群id" +
////            "type表示\t0 表示文本消息,\n" +
////            "1 表示图片，\n" +
////            "2 表示语音，\n" +
////            "3 表示视频，\n" +
////            "4 表示地理位置信息，\n" +
////            "6 表示文件，\n" +
////            "100 自定义消息类型（特别注意，对于未对接易盾反垃圾功能的应用，该类型的消息不会提交反垃圾系统检测）" +
////            "body表示消息体:例如body={\"msg\":\"hello\"}")
////    public JsonResponse sendMsg(@CurrentUser User user,@RequestParam("ope") int ope,@RequestParam("to") String to,@RequestParam("type") int type,@RequestParam("body") String body)throws Exception{
////        String url="https://api.netease.im/nimserver/msg/sendMsg.action";
////        List<NameValuePair> pairs = new ArrayList<>();
////        pairs.add(new BasicNameValuePair("from", user.getUser_id()+""));
////        pairs.add(new BasicNameValuePair("ope", ope+""));
////        pairs.add(new BasicNameValuePair("to",to));
////        pairs.add(new BasicNameValuePair("type", type+""));
////        pairs.add(new BasicNameValuePair("body", body));
////        JSONObject jsonObject=ImUtil.commonHttpRequest(url,pairs);
////        return new JsonResponse(jsonObject.get("code").toString(),"请求成功",jsonObject.get("data").toString());
////    }
//
//}