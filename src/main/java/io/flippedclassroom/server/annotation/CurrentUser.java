package io.flippedclassroom.server.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，在 Controller 的参数中注入当前登录的用户
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface CurrentUser {
}
