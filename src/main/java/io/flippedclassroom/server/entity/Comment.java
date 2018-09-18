package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * 课程评论
 */
@ApiModel(value = "课程评论实体类")
@Entity
@Data
@Accessors(chain = true)
@Table(name = "tb_comment")
public class Comment implements Serializable {
    @Id
    @GeneratedValue

    private Long comment_id;

    private String content;

    private Date comment_date;

    private Long reply = -1L;

    // 课程评论与用户的多对一关系，单向关系
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 课程评论与课程的多对一关系
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    public Comment(String content, Date date) {
        this.content = content;
        this.comment_date = date;
    }

    public Comment(String content) {
        this.content = content;
    }

    public Comment() {

    }
}
