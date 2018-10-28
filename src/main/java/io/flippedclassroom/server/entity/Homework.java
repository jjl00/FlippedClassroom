package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 课后作业
 */
@Data
@ApiModel(value = "课后作业", description = "与随堂测试题目不同")
@Entity
@Table(name = "tb_homework")
@Accessors(chain = true)
public class Homework implements Serializable {
    @Id
    @GeneratedValue
    private int homework_id;
    private String homework_content;     // 作业内容
    private String homework_answer;      // 作业答案
    @ApiModelProperty(name = "题型",notes = "分为选择题choice,填空题completion,概念题concept")
    private String question_type;//题目类型

    @ApiModelProperty(name = "该题目所属的分类",notes = "暂时没有办法统一")
    @JoinColumn(name = "course_title_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private CourseTitle homework_title;//该题目所属的分类


    // 课后作业与课程多对一关系
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;




}
