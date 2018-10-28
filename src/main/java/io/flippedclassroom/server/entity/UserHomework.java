package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


import javax.persistence.*;
import java.sql.Date;

/**
 * 描述:
 * 用户与课后作业的第三张表
 *
 * @author HASEE
 * @create 2018-09-23 13:21
 */
@Data
@Table(name = "tb_user_homework")
@Entity
@Accessors(chain = true)
@ApiModel(value = "用户与课后作业",description = "记录学生做课后题的情况")
public class UserHomework {
    @Id
    @GeneratedValue
    private int user_homework_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "homework_id")
    private Homework homework;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ApiModelProperty(name = "题目的状态",notes = "分为0,1。0代表答错,1代表正确")
    private boolean is_right=false;

    @ApiModelProperty(name = "作业提交时间")
    private Date commit_date;

}