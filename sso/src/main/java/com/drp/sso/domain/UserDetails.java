package com.drp.sso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition 对数据库的操作都是通过UserDetails
 * @date 2019year 12month04day  11:27:59
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserDetails {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "user_id")
    private String userId;
    private String username;
    private String password;
    private String nickname;
    private String gender;
    private int level;
    private long exp;
    private String email;
    @Column(name = "head_image")
    private String headImage;
    @Column(name = "create_time_in_ms")
    @CreationTimestamp
    private Timestamp createTimeInMs;
    @Column(name = "update_time_in_ms")
    @UpdateTimestamp
    private Timestamp updateTimeInMs;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_roles_rela",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private List<Role> roles;

    @JsonIgnore
    @Transient
    private boolean auth = false;//权限认证

    public boolean verifyUserAuth() {
        return auth;
    }

}
