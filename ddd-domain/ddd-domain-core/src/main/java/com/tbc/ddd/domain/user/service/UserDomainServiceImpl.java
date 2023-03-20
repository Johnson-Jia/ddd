package com.tbc.ddd.domain.user.service;

import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.north.user.service.UserDomainService;
import com.tbc.ddd.domain.user.model.Login;

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
    public void userLogin(Login login, String password) {
        // 校验密码
        login.checkPassword(password);
        // 创建session
        login.createSecret();
        // TODO 处理登录业务逻辑
    }

    @Override
    public Login userRegister(Login login) {
        return null;
    }
}
