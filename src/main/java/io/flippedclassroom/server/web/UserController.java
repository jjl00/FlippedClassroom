package io.flippedclassroom.server.web;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.config.shiro.token.PasswordToken;
import io.flippedclassroom.server.entity.Role;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.RoleService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户", description = "目前包括：用户注册、用户登录、用户登出、检查Token有效性、查看用户资料、更新用户资料")
@RestController
@CrossOrigin
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RedisService redisService;

    @PostMapping(value = "user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "{\n" +
            "\"user_name\" : \"新用户的用户名\",\n" +
            "\"password\" : \"新用户的密码\",\n" +
            "\"role\": {\"role\": \"teacher或者student\"}\n}")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse register(@RequestBody User user) throws Throwable {
        AssertUtils.assertNotNull(new Http400BadRequestException("用户基本信息不能为空！"), user.getUser_name(), user.getPassword(), user.getRole());
        //角色不存在时抛出异常
        Role role = roleService.findRoleByRoleName(user.getRole().getRole());
        if (role == null) {
            log.error("角色{}不存在", user.getRole().getRole());
        }
        String username = user.getUser_name();
        if (userService.findUserByUsername(username) != null) {
            throw new Http400BadRequestException("用户已经存在");
        }
        EncryptUtils.encrypt(user);
        user.setRole(role);
        userService.save(user);
        return new JsonResponse("200", "成功注册！", null);
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "用户名密码登录", httpMethod = "POST")
    @ApiImplicitParam(name = "user", value = "{\n" +
            "\"username\":\"1\",\n " +
            "\"password\":\"dev\" \n" +
            "}", required = true, dataType = "string", paramType = "body")
    @ApiResponses({
            @ApiResponse(code = 200, message = "自定义的 Map 的 JSON 化：\n{\n" +
                    "  \"code\" : \"200\",\n" +
                    "  \"token\" : \"Token\",\n" +
                    "  \"data\" : \"...\"\n" +
                    "}"),
            @ApiResponse(code = 401, message = "权限认证失败！"),
            @ApiResponse(code = 403, message = "身份认证失败！"),
    })
    public JsonResponse postLogin(@RequestBody User user) throws Throwable {
        String user_name = user.getUser_name();
        String password = user.getPassword();
        AssertUtils.assertNotNull(new Http400BadRequestException("用户基本信息不能为空！"), user.getUser_name(), user.getPassword());
        PasswordToken passwordToken = new PasswordToken(user_name, password);
        SecurityUtils.getSubject().login(passwordToken);
        String token = TokenUtils.sign(user_name, userService.findUserByUsername(user_name).getPassword());
        log.info("生成 Token！\n" + token);
        log.info("保存 用户名-Token 对到 Redis");
        redisService.saveWithExpire(user_name, token, DateUtils.expire(), TimeUnit.MILLISECONDS);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("user", userService.findUserByUsername(user_name).getRole().getRole());
        return new JsonResponse("200", "登录成功",map);
    }

    @GetMapping(value = "/auth/logout")
    @ApiOperation(value = "登出，会删除保存的 Token", httpMethod = "GET", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse logout(@ApiIgnore @CurrentUser User user) {
        redisService.delete(user.getUser_name());
        return new JsonResponse("200", "Logout success", null);
    }

    @GetMapping(value = "/auth/user")
    @ApiOperation(value = "用户个人资料", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
    @ApiResponses(
            @ApiResponse(code = 200, message = "用户资料：\n{\n" +
                    "  \"nickname\" : \"昵称\",\n" +
                    "  \"gender\" : \"性别\",\n" +
                    "  \"signature\" : \"个性签名\"\n" +
                    "}")
    )
    public JsonResponse getProfile(@ApiIgnore @CurrentUser User user) {
        Map<String, String> map = new HashMap<>();
        map.put("nickname", user.getNickname());
        map.put("gender", user.getGender());
        map.put("signature", user.getSignature());
        return new JsonResponse("200", "请求成功", map);
    }

    //consumes指定处理content-type
    @PutMapping(value = "/auth/user", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "修改资料", httpMethod = "PUT", notes = "不要要任何额外参数，在 HTTP 头加上 Token 请求即可")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "user", value = "{\n" +
                    "\"nickname\":\"大龙猫\",\n" +
                    "\"gender\":\"不明\",\n" +
                    "\"signature\":\"我是一只大龙猫\"\n" +
//					"\"_method\":\"put\""
                    "}", required = true, dataType = "string", paramType = "body")
    )
    @ApiResponses(
            @ApiResponse(code = 200, message = "标准的 JsonResponse，参见下方 Example Value")
    )
    public JsonResponse postProfile(@RequestBody User user, @ApiIgnore @CurrentUser User currentUser) {
        currentUser.setNickname(user.getNickname());
        currentUser.setGender(user.getGender());
        currentUser.setSignature(user.getSignature());
        userService.save(currentUser);
        return new JsonResponse("200", "成功修改资料", null);
    }
}
