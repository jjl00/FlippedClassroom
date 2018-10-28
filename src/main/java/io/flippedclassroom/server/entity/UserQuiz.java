package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 描述:
 * 用户与随堂测试的第三张表
 *
 * @author HASEE
 * @create 2018-09-22 13:09
 */
@Entity
@Data
@Table(name = "tb_user_quiz")
public class UserQuiz {
    @Id
    @GeneratedValue
    private long user_quiz_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ApiModelProperty(name = "用户提交的答案",notes = "建议使用逗号分隔")
    private String quiz_commit;
}