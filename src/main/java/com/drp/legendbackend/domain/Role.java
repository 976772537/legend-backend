package com.drp.legendbackend.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  12:31:18
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    @GeneratedValue(generator = "idGenerator")
    @Column(name = "role_id")
    private String roleId;
    @Column(name = "type")
    private String type;

    public Role(RoleType type) {
        this.type = type.toString();
    }
}
