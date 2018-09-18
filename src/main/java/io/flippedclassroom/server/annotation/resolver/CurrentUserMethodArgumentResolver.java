package io.flippedclassroom.server.annotation.resolver;

import io.flippedclassroom.server.annotation.CurrentUser;
import io.flippedclassroom.server.entity.User;
import io.flippedclassroom.server.service.UserService;
import io.flippedclassroom.server.util.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Component
@Slf4j
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(User.class) &&
                methodParameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        AuthenticationToken token = (AuthenticationToken) SecurityUtils.getSubject().getPrincipal();
        String username = (String) token.getPrincipal();
        if (username != null) {
            log.info("用户{}注入方法中", username);
            return userService.findUserByUsername(username);
        }
        throw new MissingServletRequestPartException("方法参数注入失败");
    }
}
