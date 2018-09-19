package io.flippedclassroom.server.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.flippedclassroom.server.util.Const.Const;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


/**
 * 课程信息
 */
@Table(name = "tb_course")
@Data
@ApiModel(value = "课程")
@Entity
public class Course implements Serializable {
    @Id
    @GeneratedValue
    private Long course_id;
    private String course_name;        // 课程名
    private String major;       // 课程所属专业
    private String picture;        // 课程图片
    private String code;        // 课程唯一代码

    // 课程与用户的多对多关系
    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "tb_user_course", joinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "course_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")})
    private List<User> userList;


    // 课程与课后测试一对多,mappedBy表示关系被维护端
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private List<Homework> homeworkList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private List<MyFile> myFileList;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ApiModelProperty(hidden = true)
    private List<Comment> commentList;

    public Course() {
        super();
    }

    public Course(long id,String code,String course_name,String picture ) {
        this.course_name = course_name;
        this.code=code;
        this.picture=picture;
        this.course_id=id;
    }
    public Course(String course_name,String major ) {
        this.course_name = course_name;
        this.major=major;
    }

    public String getCode() {
        return "xust" + this.code;
    }

    @Override
    public String toString() {
        return "{" +
                "course_name:'" + course_name + '\'' +
                ", picture:'" + picture + '\'' +
                ", code:'" + code + '\'' +
                '}';
    }

    public String getPicture() {
        if (picture == null) {
            picture = Const.coursePictureLink.replace("COURSEID", this.course_id.toString());
        }
        return picture;
    }

}
