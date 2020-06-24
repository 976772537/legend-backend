package com.drp.sso.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.drp.common.Const;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.drp.common.utils.DrpUtils.isNotNull;
import static com.drp.common.utils.DrpUtils.verifyImageType;
import static com.drp.common.utils.SecurityUtils.securityHmac;

/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  14:11:47
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final static String DEFALUT_HEAD_IMAGE = "http://pic2.zhimg.com/50/v2-1c3bd9fe6c6a28c5ca3a678549dfde28_hd.jpg";
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
        //set default image
        userDetails.setHeadImage(DEFALUT_HEAD_IMAGE);
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
        //TODO Cache need optimized
        if (!Cache.verifyCode(username, validCode)) {
            throw new MyAccessException("Invalid verification code");
        }
        final UserDetails userDetials =
                userRepository.findAllByUsernameAndPassword(
                        username,
                        securityHmac(password));
        isNotNull(userDetials, "user");
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

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public String changePassword(String token, String oldPassword, String newPassword) {
        final String username = JwtUtils.getUsernameFromToken(token);
        isNotNull(username, "username");
        final String secPass = securityHmac(oldPassword);
        final UserDetails user = userRepository.findAllByUsernameAndPassword(username, secPass);
        if (user == null) throw new MyAccessException("wrong old password");
        user.setPassword(securityHmac(newPassword));
        userRepository.save(user);
        return "password change successful";
    }

    @Override
    public String changeHeadImage(MultipartFile headImage, String token) throws IOException {
        //check file security
        final String type = verifyImageType(headImage.getInputStream());
        final String username = JwtUtils.getUsernameFromToken(token);
        //upload file
        String filePath = new ClassPathResource(Const.HEAD_IMAGE_PATH).getPath();
        String filename = username + "." + type;
        DrpUtils.transMultipartFileTo(headImage, filename, filePath);
        //update databases
        final UserDetails user = userRepository.findAllByUsername(username);
        isNotNull(user, "user not found");
        user.setHeadImage(filePath);
        return "头像更换成功";
    }

}
