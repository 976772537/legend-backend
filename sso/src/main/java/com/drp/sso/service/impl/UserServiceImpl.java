package com.drp.sso.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.drp.common.bean.RoleType;
import com.drp.common.bean.User;
import com.drp.common.exception.MyAccessException;
import com.drp.common.utils.Cache;
import com.drp.common.utils.DrpUtils;
import com.drp.common.utils.JwtUtils;
import com.drp.sso.domain.Role;
import com.drp.sso.domain.UserDetails;
import com.drp.sso.repository.UserRepository;
import com.drp.sso.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static com.drp.common.utils.SecurityUtils.securityHmac;

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

    @Override
    public User login(String username, String password, String validCode) {
        if (!Cache.verifyCode(username, validCode)) {
            throw new MyAccessException("Invalid verification code");
        }
        final UserDetails userDetials =
                userRepository.findAllByUsernameAndPassword(
                        username,
                        securityHmac(password));
        DrpUtils.isNotNull(userDetials, "user");
        final User user = new User();
        BeanUtil.copyProperties(userDetials, user);
        final String token = JwtUtils.generateJwtToken(user.getUsername());
        return user.setToken(token).resetPassword();
    }

    @Override
    public String generateValidCode(String username) {
        final LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 50, 4, 2);
        captcha.setGenerator(new MathGenerator());
        Cache.getCodeMap().put(username, captcha.getCode());
        return captcha.getImageBase64();
    }

    @Override
    public int authToken(String token) {
        if (JwtUtils.isTokenExpired(token)) {
            return HttpServletResponse.SC_FORBIDDEN;
        }
        final String username = JwtUtils.getUsernameFromToken(token);
        final int count = userRepository.countByUsername(username);
        return count == 1 ? HttpServletResponse.SC_OK
                : HttpServletResponse.SC_UNAUTHORIZED;
    }

}
