package com.drp.legendbackend.repository;

import com.drp.legendbackend.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  11:24:45
 */
public interface UserRepository extends JpaRepository<UserDetails, String> {

    UserDetails findAllByUserId(@Param("uid") String uid);

    UserDetails findAllByUsername(@Param("userName") String userName);
}