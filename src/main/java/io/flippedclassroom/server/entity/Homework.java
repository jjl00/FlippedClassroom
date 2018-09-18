package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 课后作业
 */
@Data
@ApiModel(value = "课后作业", description = "与随堂测试题目不同")
@Entity
public class Homework implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String content;     // 作业内容
    private String answer;      // 作业答案

    // 课后作业与课程多对一关系
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

}
