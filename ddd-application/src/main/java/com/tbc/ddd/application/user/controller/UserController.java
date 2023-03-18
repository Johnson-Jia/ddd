package com.tbc.ddd.application.user.controller;

import com.tbc.ddd.common.bean.Result;
import com.tbc.ddd.common.exception.types.AlertMsgException;
import com.tbc.ddd.common.exception.types.BadRequestException;
import com.tbc.ddd.common.exception.types.NoAuthException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tbc.ddd.application.converter.AppConverter;
import com.tbc.ddd.application.user.model.dto.LoginDTO;
import com.tbc.ddd.application.user.model.req.UserLoginReq;
import com.tbc.ddd.application.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * 用户展示层
 *
 * @author Johnson.Jia
 * @date 2023/3/16 19:52:23
 */
@RestController
@RequiredArgsConstructor
public class UserController {
    final UserService userService;
    final AppConverter appConverter;

    @PostMapping("/userLogin")
    public Result<LoginDTO> userLogin(@RequestBody UserLoginReq userLoginReq) {
        return Result.ok(userService.userLogin(appConverter.toLoginDto(userLoginReq)));
    }

}
