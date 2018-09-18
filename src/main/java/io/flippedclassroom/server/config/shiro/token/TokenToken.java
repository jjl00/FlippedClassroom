package io.flippedclassroom.server.config.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Token 校验时使用的 AuthenticationToken 类，避免多 Realms 情况下的重复校验
 */
public class TokenToken implements AuthenticationToken {
    private String username;
    private String token;

    public TokenToken() {
        super();
    }

    public TokenToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
