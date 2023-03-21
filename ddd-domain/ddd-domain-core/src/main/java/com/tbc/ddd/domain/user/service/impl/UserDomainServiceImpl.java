package com.tbc.ddd.domain.user.service.impl;

import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.south.user.repository.LoginRepository;
import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.assembler.UserAssembler;
import com.tbc.ddd.domain.user.dto.AuthUserDTO;
import com.tbc.ddd.domain.user.factory.UserAuthFactory;
import com.tbc.ddd.domain.user.factory.UserAuthService;
import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.UserInfo;
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

    final UserAuthFactory userAuthFactory;

    final LoginRepository loginRepository;
    final UserInfoRepository userInfoRepository;

    final UserAssembler userAssembler;

    @Override
    public void userLogin(Login login) {
        // TODO 处理登录领域服务 逻辑
    }

    @Override
    public Login userRegister(Login login, String code) {
        UserAuthService authService = userAuthFactory.createAuthService(login.getAuthType());
        AuthUserDTO authUser = authService.getUserInfo(code);

        login.setOpenId(authUser.getOpenId());
        login.setUnionId(authUser.getUnionId());
        login.setPassword(login.getPassword());
        login = loginRepository.save(login);

        userInfoRepository.save(UserInfo.builder().userId(login.getUserId()).gender(authUser.getGender())
            .nickName(authUser.getNickName()).avatarUrl(authUser.getAvatarUrl()).address(authUser.getAddress())
            .createTime(System.currentTimeMillis()).build());
        return login;
    }
}
