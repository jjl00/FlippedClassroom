package io.flippedclassroom.server.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 权限控制，分为学生账户、教师账户、管理员账户，暂时不考虑
 */
@Data
@ApiModel(value = "权限实体类", description = "考虑依据权限类区分登录、未登录用户或管理员、普通用户，提供试用和管理功能")
@Entity
@Table(name = "tb_permission")
public class Permission implements Serializable {
    @Id
    @GeneratedValue
    private Long permission_id;
    private String permission;      // 权限名

//    // 权限与角色的多对多关系
//    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
//    @JoinTable(name = "tb_role_permission", joinColumns = {@JoinColumn(name = "permission_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
//    private List<Role> roleList;
}
