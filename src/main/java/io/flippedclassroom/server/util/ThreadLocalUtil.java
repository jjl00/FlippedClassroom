package io.flippedclassroom.server.util;

/**
 * 描述:
 * 用在存放全局变量
 *
 * @author HASEE
 * @create 2018-09-18 14:13
 */
public interface ThreadLocalUtil {
    ThreadLocal<String> current_username=new ThreadLocal<>();
}