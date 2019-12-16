package com.drp.sso.service;


import com.drp.common.bean.User;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  14:10:43
 */
public interface UserService {
    /**
     * USER register
     */
    User register(User user);

    User login(String username, String password, String validCode);

    String generateValidCode(String username);

    int authToken(String token);
}
