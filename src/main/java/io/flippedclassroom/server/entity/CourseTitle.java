package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * 描述:
 * 课程标题
 *
 * @author HASEE
 * @create 2018-09-28 15:24
 */
@Entity
@Table(name = "tb_course_title")
@ApiModel(value = "课程标题（课后作业包名）",description = "标题属于课后作业的一部分")
@Data
@Accessors(chain = true)
public class CourseTitle implements Comparable<CourseTitle> ,Serializable {
    @Id
    @GeneratedValue
    private long course_title_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    private String course_title_name;
    @ApiModelProperty(value = "创建日期")
    private Date course_title_date;   //创建日期
    @ApiModelProperty(value = "课后作业的总数")
    private int  homework_sum;        //课后作业的总数



    @Override
    public int compareTo(CourseTitle o) {
        return course_title_name.compareTo(o.course_title_name);
    }
}