package io.flippedclassroom.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述:
 * 测试消息
 *
 * @author HASEE
 * @create 2018-09-18 9:58
 */
@Data
@AllArgsConstructor
public class Message implements Serializable {
    public String data;
}