package com.drp.legendbackend.controller;

import com.drp.legendbackend.bean.User;
import com.drp.legendbackend.common.ActionUri;
import com.drp.legendbackend.common.RootUri;
import com.drp.legendbackend.common.utils.NetworkResult;
import com.drp.legendbackend.common.utils.RsHelper;
import com.drp.legendbackend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    public Callable<NetworkResult<Object>> userRegister(@RequestBody @Valid User user){
        return () -> RsHelper.success( userService.register(user));
    }
}
