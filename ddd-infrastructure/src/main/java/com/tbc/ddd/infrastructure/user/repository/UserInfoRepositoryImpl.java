package com.tbc.ddd.infrastructure.user.repository;

import org.springframework.stereotype.Repository;

import com.tbc.ddd.domain.south.user.repository.UserInfoRepository;
import com.tbc.ddd.domain.user.model.UserId;
import com.tbc.ddd.domain.user.model.UserInfoDO;
import com.tbc.ddd.infrastructure.user.converter.UserInfoConverter;
import com.tbc.ddd.infrastructure.user.entity.UserInfoPO;
import com.tbc.ddd.infrastructure.user.mapper.UserInfoMapper;

import lombok.RequiredArgsConstructor;

/**
 * 用户仓储服务
 *
 * @author Johnson.Jia
 * @date 2023/3/15 11:03:34
 */
@Repository
@RequiredArgsConstructor
public class UserInfoRepositoryImpl implements UserInfoRepository {

    final UserInfoMapper userInfoMapper;
    final UserInfoConverter userInfoConverter;

    @Override
    public void deleteById(UserId userId) {
        userInfoMapper.deleteById(userId.getId());
    }

    @Override
    public UserInfoDO getById(UserId userId) {
        UserInfoPO userInfoPO = userInfoMapper.selectById(userId.getId());
        return userInfoConverter.toUserInfoDo(userInfoPO);
    }

    @Override
    public UserInfoDO save(UserInfoDO userInfoDO) {
        UserInfoPO userInfoPO = userInfoConverter.toUserInfoPo(userInfoDO);
        if (userInfoDO.getUserId() != null) {
            userInfoMapper.updateById(userInfoPO);
        } else {
            userInfoMapper.insert(userInfoPO);
        }
        return userInfoConverter.toUserInfoDo(userInfoPO);
    }
}
