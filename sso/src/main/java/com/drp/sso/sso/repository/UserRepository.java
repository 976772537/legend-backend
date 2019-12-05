package com.drp.sso.sso.repository;

import com.drp.sso.sso.common.RootUri;
import com.drp.sso.sso.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  11:24:45
 */
@RepositoryRestResource(path = RootUri.USER_RESOURCE)
public interface UserRepository extends JpaRepository<UserDetails, String> {

    int countByNickname(String nickName);

    int countByUsername(String userName);

    @RestResource(exported = false)
    UserDetails findAllByUserId(@Param("uid") String uid);

    @RestResource(exported = false)
    UserDetails findAllByUsername(@Param("userName") String userName);
}