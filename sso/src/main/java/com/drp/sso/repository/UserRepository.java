package com.drp.sso.repository;

import com.drp.sso.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  11:24:45
 */
public interface UserRepository extends JpaRepository<UserDetails, String> {
    int countByUsername(String userName);
    UserDetails findAllByUsernameAndPassword(String username, String password);

    UserDetails findAllByUsername(String username);
}