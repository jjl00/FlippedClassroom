package io.flippedclassroom.server.config.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用户名密码 校验时使用的 AuthenticationToken 类，避免多 Realms 情况下的重复校验
 */
public class PasswordToken implements AuthenticationToken {
    private String username;
    private String password;

    public PasswordToken() {
        super();
    }

    public PasswordToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}
