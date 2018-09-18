package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.flippedclassroom.server.util.Const.FileType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.sql.Date;

/**
 * 描述:
 * 上传文件
 *
 * @author HASEE
 * @create 2018-09-11 12:45
 */
@Data
@Slf4j
@Entity
@Accessors(chain = true)
@Table(name = "tb_file")
@ApiModel("文件")
public class MyFile {
    @Id
    @GeneratedValue
    long file_id;
    @JsonIgnore
    private String position;  //文件位置
    private long size;       //文件大小
    private String file_name; //文件名字
    private Date file_date;   //文件日期
    private String file_author; //文件上传者
    @ApiModelProperty(value = "文件格式:video,picture...")
    private String fileFormat;  //文件格式:video,picture...
    @ApiModelProperty(value = "文件类别如预习资料，文档之类")
    private String fileType;    //分为预习资料之类的

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


}