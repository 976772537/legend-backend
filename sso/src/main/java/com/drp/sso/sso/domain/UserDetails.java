package com.drp.sso.sso.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition
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
    private char gender;
    private int level;
    private long exp;
    private String email;
    @Column(name = "head_image")
    private String headImage;
    @Column(name = "create_time_in_ms")
    private long createTimeInMs;
    @Column(name = "update_time_in_ms")
    private long updateTimeInMs;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_roles_rela",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private List<Role> roles;

    public boolean verifyUserAuth() {
        return false;
    }

}
