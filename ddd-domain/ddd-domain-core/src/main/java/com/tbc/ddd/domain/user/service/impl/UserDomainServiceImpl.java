package com.tbc.ddd.domain.user.service.impl;

import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.service.UserDomainService;

import lombok.RequiredArgsConstructor;

/**
 * 用户领域服务
 *
 * @author Johnson.Jia
 * @date 2023/3/17 18:11:56
 */
@Service
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    @Override
    public void userLogin(Login login) {

        // TODO 处理登录领域服务 逻辑
    }

    @Override
    public Login userRegister(Login login) {
        // TODO 处理注册领域服务 逻辑
        return null;
    }

}
