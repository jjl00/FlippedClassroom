package io.flippedclassroom.server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "角色实体类")
@Entity
@ToString
@Data
@Table(name = "tb_role")
public class Role implements Serializable {
    @Id
    @GeneratedValue
    private Long role_id;

    private String role;    // 角色名

    // 角色与权限的多对多关系
//    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
//    @JoinTable(name = "tb_role_permission", joinColumns = {@JoinColumn(name = "role_id")}, inverseJoinColumns = {@JoinColumn(name = "permission_id")})
//    private List<Permission> permissionList;

    public Role() {
        super();
    }

    public Role(String role) {
        this.role = role;
    }
}

