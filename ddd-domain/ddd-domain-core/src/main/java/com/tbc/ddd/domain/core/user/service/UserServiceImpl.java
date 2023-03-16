package com.tbc.ddd.domain.core.user.service;

import com.tbc.ddd.domain.user.model.LoginDO;
import com.tbc.ddd.domain.user.model.Phone;
import com.tbc.ddd.domain.user.model.UserId;
import org.springframework.stereotype.Service;

import com.tbc.ddd.domain.user.aggregate.UserAggRoot;
import com.tbc.ddd.domain.user.model.UserInfoDO;
import com.tbc.ddd.domain.north.user.service.UserService;
import com.tbc.ddd.domain.south.role.repository.MenusRepository;
import com.tbc.ddd.domain.south.role.repository.RoleRepository;
import com.tbc.ddd.domain.south.user.repository.LoginRepository;
import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;

import lombok.RequiredArgsConstructor;

/**
 * 用户服务接口
 *
 * @author Johnson.Jia
 * @date 2023/3/15 17:01:31
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final LoginRepository loginRepository;
    final UserInfoRepository userInfoRepository;
    final RoleRepository roleRepository;
    final MenusRepository menusRepository;

    @Override
    public UserAggRoot getUserById(UserId userId) {
        LoginDO loginDO = loginRepository.getById(userId);
        UserInfoDO userInfoDO = userInfoRepository.getById(userId);
        return UserAggRoot.builder().loginDO(loginDO).userInfoDO(userInfoDO).build();
    }

    @Override
    public UserAggRoot getUserByPhone(Phone phone) {
        LoginDO loginDO = loginRepository.getByPhone(phone);
        UserInfoDO userInfoDO = userInfoRepository.getById(loginDO.getUserId());
        return UserAggRoot.builder().loginDO(loginDO).userInfoDO(userInfoDO).build();
    }
}
