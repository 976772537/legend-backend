package com.drp.legendbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.drp.legendbackend.bean.User;
import com.drp.legendbackend.common.exception.WrongCodeException;
import com.drp.legendbackend.common.utils.JwtUtils;
import com.drp.legendbackend.domain.Role;
import com.drp.legendbackend.domain.RoleType;
import com.drp.legendbackend.domain.UserDetails;
import com.drp.legendbackend.repository.UserRepository;
import com.drp.legendbackend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;

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

        final ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role(RoleType.normal));
        userDetails.setRoles(roles);
        final long time = System.currentTimeMillis();
        userDetails.setCreateTimeInMs(time);
        userDetails.setUpdateTimeInMs(time);

        userDetails = userRepository.save(userDetails);

        if(userDetails == null) {
            throw new WrongCodeException("userDetails => null");
        }

        final String token = JwtUtils.generateJwtToken(userDetails.getUsername());
        return user.setToken(token).resetPassword();
    }
}
