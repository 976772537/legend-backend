package com.drp.sso.sso.repository;

import com.drp.sso.sso.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  11:24:45
 */
public interface UserRepository extends JpaRepository<UserDetails, String> {

    int countByNickname(String nickName);

    int countByUsername(String userName);

    UserDetails findAllByUserId(String uid);

    UserDetails findAllByUsername(String username);
}