package io.flippedclassroom.server.advice.exception;

import io.flippedclassroom.server.entity.JsonResponse;
import io.flippedclassroom.server.exception.Http400BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 控制器建言，全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(value = AuthenticationException.class)
    public JsonResponse handle403() {
        return new JsonResponse("403", "身份认证失败！", null);
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public JsonResponse handle401() {
        return new JsonResponse("401", "权限鉴定失败！", null);
    }

    @ExceptionHandler(value = Http400BadRequestException.class)
    public JsonResponse handleBadRequestException(Exception ex) {
        return new JsonResponse("400", ex.getMessage(), null);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public JsonResponse handleMethodNotSupportedException(Exception ex) {
        return new JsonResponse("405", ex.getMessage(), null);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public JsonResponse handleException(Exception ex) {
        log.info("exception!!");
        ex.printStackTrace();
        return new JsonResponse("400", ex.getMessage(), null);
    }
}
