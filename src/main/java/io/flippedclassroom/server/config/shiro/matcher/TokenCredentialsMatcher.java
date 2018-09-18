package io.flippedclassroom.server.config.shiro.matcher;

import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.RedisService;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义的 Token 校验类，通过验证 Token 实现校验
 */
@Slf4j
public class TokenCredentialsMatcher extends SimpleCredentialsMatcher {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String principal = (String) token.getPrincipal();
        String credentials = (String) token.getCredentials();
        String redisCheck = redisService.get(principal);
        if (redisCheck == null) {
            log.error("token已失效");
            return false;
        } else if (!redisCheck.equals(credentials)) {
            log.error("用户{}认证失败", principal);
            return false;
        } else {
            User user = userService.findUserByUsername(principal);
            return TokenUtils.verify(credentials, user.getUser_name(), user.getPassword());
        }

    }
}