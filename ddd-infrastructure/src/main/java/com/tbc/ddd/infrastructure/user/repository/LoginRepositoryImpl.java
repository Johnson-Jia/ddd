package com.tbc.ddd.infrastructure.user.repository;

import com.tbc.ddd.domain.user.model.Login;
import com.tbc.ddd.domain.user.model.Phone;
import com.tbc.ddd.domain.user.model.UserId;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tbc.ddd.domain.south.user.repository.LoginRepository;
import com.tbc.ddd.infrastructure.user.converter.LoginConverter;
import com.tbc.ddd.infrastructure.user.entity.LoginPO;
import com.tbc.ddd.infrastructure.user.mapper.LoginMapper;

import lombok.RequiredArgsConstructor;

/**
 * 用户登录信息
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:48:47
 */
@Repository
@RequiredArgsConstructor
public class LoginRepositoryImpl implements LoginRepository {

    final LoginMapper loginMapper;
    final LoginConverter loginConverter;

    @Override
    public void deleteById(UserId userId) {
        loginMapper.deleteById(userId.getId());
    }

    @Override
    public Login getById(UserId userId) {
        return loginConverter.toLogin(loginMapper.selectById(userId.getId()));
    }

    @Override
    public Login save(Login login) {
        LoginPO loginPO = loginConverter.toLoginPo(login);
        if (login.getUserId() != null) {
            loginMapper.insert(loginPO);
        } else {
            loginMapper.updateById(loginPO);
        }
        return loginConverter.toLogin(loginPO);
    }

    @Override
    public Login getByPhone(Phone phone) {
        return loginConverter
            .toLogin(loginMapper.selectOne(Wrappers.<LoginPO>lambdaQuery().eq(LoginPO::getPhone, phone.getPhone())));
    }
}
