package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
@Accessors(chain = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_notice")
@ApiModel(value = "通知或公告",description = "某课程下的公告")
public class Notice implements Serializable {
    @Id
    @GeneratedValue
    public Long notice_id;
    @ApiModelProperty(notes = "目前分为公告与作业通知")
    String notice_type;   //通知类型
    public String notice_content;
    public Date notice_date;
    public String originator; //发起人


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonIgnore
    public Course course;
}
