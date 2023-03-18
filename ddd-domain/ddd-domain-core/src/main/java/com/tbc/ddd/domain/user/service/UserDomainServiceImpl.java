package com.tbc.ddd.domain.user.service;

import java.util.UUID;

import org.apache.dubbo.config.annotation.DubboService;

import com.tbc.ddd.common.bean.Secret;
import com.tbc.ddd.domain.north.user.service.UserDomainService;
import com.tbc.ddd.domain.user.model.Login;

import lombok.RequiredArgsConstructor;

/**
 * 用户领域服务
 *
 * @author Johnson.Jia
 * @date 2023/3/17 18:11:56
 */
@DubboService
@RequiredArgsConstructor
public class UserDomainServiceImpl implements UserDomainService {

    @Override
    public Secret userLogin(Login login, String password) {
        login.checkPassword(password);
        Secret build = Secret.builder().sessionId(UUID.randomUUID().toString())
            .secretKey(UUID.randomUUID().toString()).build();
        return build;
    }
}
