package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * 描述:
 * 用户和通知构成的第三张表
 *
 * @author HASEE
 * @create 2018-09-18 10:40
 */
@ApiModel(value = "用户和通知构成的第三张表")
@Entity
@Table(name = "tb_user_notice")
public class UserNotice {
    @Id
    @GeneratedValue
    private long user_notice_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ApiModelProperty(notes = "是否已读")
    private  boolean is_read=false;


}