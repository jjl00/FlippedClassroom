package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;

/**
 * 描述:
 * 考勤情况
 *
 * @author HASEE
 * @create 2018-09-11 16:14
 */
@Data
@Entity

@Table(name = "tb_attendance")
@ApiModel(value = "考勤")
public class Attendance {
    @Id
    @GeneratedValue
    private long attendance_id;
    @ApiModelProperty("缺勤次数")
    private int absenteeism;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


}