package com.drp.legendbackend.repository.rest;

import com.drp.legendbackend.common.RootUri;
import com.drp.legendbackend.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author dongruipeng
 * @Descrpition resource of user
 * @date 2019year 12month04day  15:54:23
 */
@RepositoryRestResource(path = RootUri.USER_RESOURCE)
public interface UserResource extends JpaRepository<UserDetails, String> {

    int countByNickname(String nickName);

    int countByUsername(String userName);

}
