package io.flippedclassroom.server.filter;

import io.flippedclassroom.server.config.shiro.token.TokenToken;
import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.util.JsonUtils;
import io.flippedclassroom.server.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
        httpResponse.setStatus(HttpStatus.OK.value());
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException, AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        String username = null;
        if (authorization != null) {
            try {
                username = TokenUtils.getUsername(authorization);
                TokenToken tokenToken = new TokenToken(username, authorization);
                getSubject(request, response).login(tokenToken);
                log.info("{}验证通过", username);
                return true;
            } catch (AuthenticationException e) {
                log.error("{}认证失败", username);
                throw new AuthenticationException(username + "认证失败");
            }
        } else {
            log.error("token为空");
            throw new AuthenticationException("token为空");

        }
    }


}
