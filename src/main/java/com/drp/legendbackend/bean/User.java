package com.drp.legendbackend.bean;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  14:26:32
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel
public class User implements Serializable {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    private char gender;
    @Min(value = 0)
    private int level;
    @Min(value = 0)
    private long exp;
    @Email(message = "请输入正确的邮箱格式")
    private String email;
    private String headImage;
    private String token;


    public User setToken(String token) {
        this.token = token;
        return this;
    }

    public User resetPassword() {
        this.password = null;
        return this;
    }
}
