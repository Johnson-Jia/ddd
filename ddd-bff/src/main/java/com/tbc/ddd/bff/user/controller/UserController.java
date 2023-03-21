package com.tbc.ddd.bff.user.controller;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tbc.ddd.bff.user.converter.UserConverter;
import com.tbc.ddd.bff.user.model.req.LoginByNameReq;
import com.tbc.ddd.bff.user.model.req.LoginByPhoneReq;
import com.tbc.ddd.bff.user.model.req.UserRegisterReq;
import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.spring.BaseController;
import com.tbc.ddd.domain.north.user.application.UserApplicationService;
import com.tbc.ddd.domain.user.dto.UserDTO;

import lombok.RequiredArgsConstructor;

/**
 * 用户展示层
 *
 * @author Johnson.Jia
 * @date 2023/3/16 19:52:23
 */
@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    final UserApplicationService userApplicationService;
    final UserConverter userConverter;

    @PostMapping("/loginByPhone")
    public Result<UserDTO> loginByPhone(@RequestBody @Valid LoginByPhoneReq login) {
        return Result.ok(userApplicationService.loginByPhone(login.getPhone(), login.getPassword()));
    }

    @PostMapping("/loginByName")
    public Result<UserDTO> loginByName(@RequestBody @Valid LoginByNameReq login) {
        return Result.ok(userApplicationService.loginByName(login.getLoginName(), login.getPassword()));
    }

    @PostMapping("/userRegister")
    public Result<UserDTO> userRegister(@RequestBody @Validated UserRegisterReq registerReq) {
        return Result.ok(userApplicationService.userRegister(userConverter.toUserRegisterDto(registerReq)));
    }

}
