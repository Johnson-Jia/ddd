package com.tbc.ddd.application.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tbc.ddd.domain.north.user.service.UserService;
import com.tbc.ddd.domain.user.aggregate.UserAggRoot;
import com.tbc.ddd.domain.user.model.UserId;

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

    @GetMapping("/getUser/{userId}")
    public UserAggRoot getUserInfo(@PathVariable Long userId) {
        return userService.getUserById(UserId.builder().id(userId).build());
    }
}
