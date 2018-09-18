package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "tb_user")
@ApiModel(value = "用户")
@Entity
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long user_id;

    @Column(nullable = false)
    private String user_name;    // 用户名

    @Column(nullable = false)
    private String password;    // 密码加盐 hash
    @JsonIgnore
    private String salt;        // 每个用户唯一的盐
    private String nickname;   // 昵称
    private String avatar;      // 头像文件地址
    private String gender = "男";      // 性别
    private String signature = "";   // 个性签名


    @ManyToOne(fetch = FetchType.EAGER)
    @Lazy(value = false)
    @JoinColumn(name = "role_id")
    private Role role;

    // 用户与课程的多对多关系，通过表 `tb_user_course` 维持
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},fetch = FetchType.EAGER)
    @JoinTable(name = "tb_user_course", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "course_id", referencedColumnName = "course_id")})
    @JsonIgnore
    private List<Course> courseList;

    public User() {
        super();
    }

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public User(String nickname, String gender, String signature) {
        this.nickname = nickname;
        this.gender = gender;
        this.signature = signature;
    }

    public String getNickname() {
        if (nickname == null) {
            return user_name;
        } else {
            return nickname;
        }
    }
}
