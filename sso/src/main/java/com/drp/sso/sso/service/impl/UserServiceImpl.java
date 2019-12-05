package com.drp.sso.sso.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.drp.common.common.bean.User;
import com.drp.common.common.utils.JwtUtils;
import com.drp.sso.sso.domain.Role;
import com.drp.common.common.bean.RoleType;
import com.drp.sso.sso.domain.UserDetails;
import com.drp.sso.sso.repository.UserRepository;
import com.drp.sso.sso.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

import static com.drp.common.common.utils.SecurityUtils.securityHmac;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  14:11:47
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User register(User user) {
        final CopyOptions copyOptions = CopyOptions.create().setIgnoreNullValue(true);
        UserDetails userDetails = new UserDetails();
        BeanUtil.copyProperties(user, userDetails, copyOptions);
        //set roles
        final ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleType.normal));
        userDetails.setRoles(roles);
        //set time
        final long time = System.currentTimeMillis();
        userDetails.setCreateTimeInMs(time);
        userDetails.setUpdateTimeInMs(time);
        //secure password
        final String password = securityHmac(user.getPassword());
        userDetails.setPassword(password);

        userDetails = userRepository.save(userDetails);
        //generate token
        final String token = JwtUtils.generateJwtToken(userDetails.getUsername());
        user.setHeadImage(userDetails.getHeadImage());
        return user.resetPassword().setToken(token);
    }
}
