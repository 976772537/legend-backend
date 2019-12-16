package com.drp.sso.controller;

import com.drp.common.ActionUri;
import com.drp.common.RootUri;
import com.drp.common.bean.User;
import com.drp.common.utils.NetworkResult;
import com.drp.common.utils.RsHelper;
import com.drp.sso.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.concurrent.Callable;


/**
 * @author dongruipeng
 * @Descrpition
 * @date 2019year 12month04day  14:40:18
 */
@RequestMapping(RootUri.USER)
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "register")
    @PostMapping(ActionUri.SAVE)
    public Callable<NetworkResult<Object>> userRegister(@RequestBody @Valid User user) {
        return () -> RsHelper.success(userService.register(user));
    }

    @ApiOperation(value = "login")
    @GetMapping(ActionUri.LOGIN)
    public Callable<NetworkResult<Object>> userLogin(@RequestParam @NotBlank String username,
                                                     @RequestParam @NotBlank String password,
                                                     @RequestParam @NotBlank String validCode) {
        return () -> RsHelper.success(userService.login(username, password, validCode));
    }

    @ApiOperation(value = "valid code")
    @GetMapping(ActionUri.VALID_CODE)
    public Callable<String> getValidCode(@RequestParam @NotBlank String username) {
        return () -> userService.generateValidCode(username);
    }

    @ApiOperation(value = "auth Token")
    @GetMapping(ActionUri.AUTH_TOKEN)
    public Callable<Integer> authToken(@RequestParam @NotBlank String token) {
        return () -> userService.authToken(token);
    }
}
