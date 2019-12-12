package com.drp.common.domain;

import com.drp.common.exception.MyAccessException;
import com.drp.common.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SignatureException;
import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month12day  16:33:26
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationChian {
    private String token;
    private String url;
    private List<String> permitPaths;

    public AuthorizationChian isToken() {
        try {
            JwtUtils.getUsernameFromHeader(token);
            return this;
        } catch (Exception e) {
            throw new MyAccessException("is not a vaild token");
        }
    }

    public AuthorizationChian isExpired() {
        if (JwtUtils.isTokenExpired(token)) {
            return this;
        }
        throw new MyAccessException("token is expired");
    }
}
